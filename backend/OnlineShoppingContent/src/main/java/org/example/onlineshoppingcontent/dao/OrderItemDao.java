package org.example.onlineshoppingcontent.dao;

import org.example.onlineshoppingcontent.model.OrderItem;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class OrderItemDao extends AbstractHibernateDao<OrderItem> {

    public OrderItemDao() {
        setClazz(OrderItem.class);
    }

    public void saveOrderItem(OrderItem orderItem) {
        this.save(orderItem);
    }

    public OrderItem getById(Long orderId) {
        return this.getById(orderId);
    }

    public List<OrderItem> getAllOrderItemsByOrderId(Long orderId) {
        String hql = "FROM OrderItem WHERE order.orderId = :orderId";
        Query<OrderItem> query = getCurrentSession().createQuery(hql, OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public Long getProductId(Long orderId) {
        return getById(orderId).getProduct().getProductId();
    }
}