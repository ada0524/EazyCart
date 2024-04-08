package org.example.onlineshoppingcontent.dao;

import org.example.onlineshoppingcontent.model.Order;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao extends AbstractHibernateDao<Order> {

    @PersistenceContext
    private EntityManager entityManager;

    public OrderDao() {
        setClazz(Order.class);
    }

    public void saveOrder(Order order) {
        this.save(order);
    }

    public List<Map<String, Object>> getOrdersByUserId(Long userId) {
        Criteria criteria = getCurrentSession().createCriteria(Order.class);
        criteria.add(Restrictions.eq("user.userId", userId));

        ProjectionList projectionList = Projections.projectionList()
                .add(Projections.property("orderId"), "orderId")
                .add(Projections.property("datePlaced"), "datePlaced")
                .add(Projections.property("orderStatus"), "orderStatus");

        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return criteria.list();
    }

    public List<Map<String, Object>> getAllOrders() {
        Criteria criteria = getCurrentSession().createCriteria(Order.class);

        ProjectionList projectionList = Projections.projectionList()
                .add(Projections.property("orderId"), "orderId")
                .add(Projections.property("datePlaced"), "datePlaced")
                .add(Projections.property("orderStatus"), "orderStatus");

        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return criteria.list();
    }

     public Order getOrderById(Long orderId) {
        return this.getById(orderId);
    }

    public void updateOrderStatus(Long orderId, String newStatus) {
        String hql = "UPDATE Order o SET o.orderStatus = :newStatus WHERE o.orderId = :orderId";
        entityManager.createQuery(hql)
                .setParameter("newStatus", newStatus)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}