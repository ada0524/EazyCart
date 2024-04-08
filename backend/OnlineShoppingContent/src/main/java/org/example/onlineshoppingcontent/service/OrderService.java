package org.example.onlineshoppingcontent.service;


import org.example.onlineshoppingcontent.dao.OrderDao;

import org.example.onlineshoppingcontent.dao.OrderItemDao;
import org.example.onlineshoppingcontent.dao.ProductDao;
import org.example.onlineshoppingcontent.dao.UserDao;
import org.example.onlineshoppingcontent.dto.user.request.OrderItemRequest;
import org.example.onlineshoppingcontent.dto.user.request.OrderRequest;
import org.example.onlineshoppingcontent.exception.NotEnoughInventoryException;
import org.example.onlineshoppingcontent.exception.OrderNotCancelableException;
import org.example.onlineshoppingcontent.exception.OrderNotCompletableException;
import org.example.onlineshoppingcontent.model.Order;
import org.example.onlineshoppingcontent.model.OrderItem;
import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.model.User;
import org.example.onlineshoppingcontent.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public OrderService(OrderDao orderDao, OrderItemDao orderItemDao, UserDao userDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof String)) {
            return null;
        }
        String username = (String) authentication.getPrincipal();
        return userDao.getUserByUsername(username);
    }

    @Transactional(rollbackFor = NotEnoughInventoryException.class)
    public void placeOrder(OrderRequest orderRequest) throws NotEnoughInventoryException {
        // Create the Order entity
        Order order = new Order();
        order.setDatePlaced(LocalDateTime.now());
        order.setOrderStatus("Processing");
        order.setUser(getCurrentUser());
        orderDao.saveOrder(order);

        // Create the OrderItem entity
        for (OrderItemRequest orderItemRequest : orderRequest.getOrder()) {
            long productId = orderItemRequest.getProductId();
            int quantity = orderItemRequest.getQuantity();
            Product product = productDao.getProductById(productId);
            if (quantity > product.getQuantity()) {
                throw new NotEnoughInventoryException("Not enough inventory for product with ID: " + productId);
            }
            OrderItem orderItem = new OrderItem(product.getRetailPrice(), quantity, product.getWholesalePrice(), order, product);
            orderItemDao.saveOrderItem(orderItem);
            product.setQuantity(product.getQuantity() - quantity);
            productDao.saveProduct(product);
        }
    }

    public List<Map<String, Object>> getAllUserReadOrders() {
        return orderDao.getOrdersByUserId(getCurrentUser().getUserId());
    }

    public List<Map<String, Object>> getAllAdminReadOrders() {
        return orderDao.getAllOrders();
    }

    public Map<String, Object> getOrderById(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItems = orderItemDao.getAllOrderItemsByOrderId(orderId);

        return mapToOrderDetailMap(order, orderItems);
    }

    private Map<String, Object> mapToOrderDetailMap(Order order, List<OrderItem> orderItems) {
        Map<String, Object> orderMap = new LinkedHashMap<>();
        orderMap.put("orderId", order.getOrderId());
        orderMap.put("datePlaced", order.getDatePlaced());
        orderMap.put("orderStatus", order.getOrderStatus());

        List<Map<String, Object>> orderItemList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Map<String, Object> orderItemMap = new LinkedHashMap<>();
            orderItemMap.put("itemId", orderItem.getItemId());
            orderItemMap.put("purchasedPrice", orderItem.getPurchasedPrice());
            orderItemMap.put("quantity", orderItem.getQuantity());

            orderItemList.add(orderItemMap);
        }
        orderMap.put("orderItems", orderItemList);

        return orderMap;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order.getOrderStatus().equals("Processing")) {
            orderDao.updateOrderStatus(orderId, "Canceled");
            for (OrderItem orderItem : order.getOrderItems()) {
                productDao.addProductQuantity(orderItem.getProduct().getProductId(), orderItem.getQuantity());
            }
        }
        else {
            throw new OrderNotCancelableException("Not cancelable order.");
        }
    }

    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order.getOrderStatus().equals("Processing")) {
            orderDao.updateOrderStatus(orderId, "Completed");
        }
        else {
            throw new OrderNotCompletableException("Not cancelable order.");
        }
    }
}