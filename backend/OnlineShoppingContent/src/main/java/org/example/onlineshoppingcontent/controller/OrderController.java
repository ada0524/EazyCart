package org.example.onlineshoppingcontent.controller;

import org.example.onlineshoppingcontent.dto.user.request.OrderRequest;
import org.example.onlineshoppingcontent.exception.NotEnoughInventoryException;
import org.example.onlineshoppingcontent.exception.OrderNotCancelableException;
import org.example.onlineshoppingcontent.exception.OrderNotCompletableException;
import org.example.onlineshoppingcontent.model.Order;
import org.example.onlineshoppingcontent.service.OrderService;
import org.example.onlineshoppingcontent.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderService.placeOrder(orderRequest);
            return new ResponseEntity<>("Order placed successfully!", HttpStatus.CREATED);
        } catch (NotEnoughInventoryException e) {
            throw new NotEnoughInventoryException("The inventory storage is insufficient. Please adjust your order accordingly.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("user_read"))) {
            List<Map<String, Object>> userOrders = orderService.getAllUserReadOrders();
            return new ResponseEntity<>(userOrders, HttpStatus.OK);
        } else if (authorities.contains(new SimpleGrantedAuthority("admin_read"))) {
            List<Map<String, Object>> adminOrders = orderService.getAllAdminReadOrders();
            return new ResponseEntity<>(adminOrders, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long orderId) {
        Map<String, Object> order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return new ResponseEntity<>("Order canceled successfully.", HttpStatus.OK);
        }
        catch (OrderNotCancelableException e) {
            throw new OrderNotCancelableException("Order cannot be canceled as it has already been completed.");
        }
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        try {
            orderService.completeOrder(orderId);
            return new ResponseEntity<>("Order completed successfully.", HttpStatus.OK);
        }
        catch (OrderNotCompletableException e) {
            throw new OrderNotCancelableException("Order cannot be completed as it has already been canceled.");
        }
    }
}