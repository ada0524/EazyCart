package org.example.onlineshoppingcontent.dao;

import org.example.onlineshoppingcontent.model.OrderItem;
import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.model.User;
import org.example.onlineshoppingcontent.security.AuthUserDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDao extends AbstractHibernateDao<Product> {

    @PersistenceContext
    private EntityManager entityManager;

    public ProductDao() {
        setClazz(Product.class);
    }

    public void saveProduct(Product product) {
        this.save(product);
    }

    public Product getProductById(Long productId) {
        return this.getById(productId);
    }

    public List<Product> getAllProducts() {
        return this.getAll();
    }

    public void addProductQuantity(long productId, int quantity) {
        Session session = entityManager.unwrap(Session.class);

        String hql = "UPDATE Product SET quantity = quantity + :quantity WHERE productId = :productId";
        session.createQuery(hql)
                .setParameter("quantity", quantity)
                .setParameter("productId", productId)
                .executeUpdate();
    }

    @Transactional
    public List<Object[]> getTopThreeMostFrequentlyPurchasedItems(Long userId) {
        String hql = createFrequentItemsHQL();
        Query<Object[]> query = this.getCurrentSession().createQuery(hql, Object[].class);
        query.setParameter("userId", userId);
        return query.setMaxResults(3).getResultList();
    }

    private String createFrequentItemsHQL() {
        // determine return value to avoid lazy loading problem
        return "SELECT p.productId, p.name, COUNT(oi) " +
                "FROM OrderItem oi " +
                "JOIN Product p ON oi.product.productId = p.productId " +
                "JOIN Order o ON oi.order.orderId = o.orderId " +
                "WHERE o.user.userId = :userId AND o.orderStatus <> 'Canceled' " +
                "GROUP BY p.id ORDER BY COUNT(oi) DESC, p.id ASC";
    }

    public List<Object[]> getTopThreeMostRecentlyPurchasedItems(Long userId) {
        String hql = createRecentItemsHQL();
        Query<Object[]> query = this.getCurrentSession().createQuery(hql, Object[].class);
        query.setParameter("userId", userId);
        return query.setMaxResults(3).getResultList();
    }

    private String createRecentItemsHQL() {
        return "SELECT p.productId, p.name, COUNT(oi) " +
                "FROM OrderItem oi " +
                "JOIN Product p ON oi.product.productId = p.productId " +
                "JOIN Order o ON oi.order.orderId = o.orderId " +
                "WHERE o.user.userId = :userId AND o.orderStatus <> 'Canceled' " +
                "GROUP BY p.id, o.datePlaced " +
                "ORDER BY o.datePlaced DESC, COUNT(oi) DESC, p.id ASC";
    }

    public List<Object[]> getTopThreeMostProfitableItems() {
        String hql = createProfitableItemsHQL();
        Query<Object[]> query = this.getCurrentSession().createQuery(hql, Object[].class);
        return query.setMaxResults(3).getResultList();
    }

    private String createProfitableItemsHQL() {
        return "SELECT p.productId, p.name, SUM((oi.product.retailPrice - oi.product.wholesalePrice) * oi.quantity) AS totalProfit " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "JOIN oi.product p " +
                "WHERE o.orderStatus <> 'Processing' AND o.orderStatus <> 'Canceled' " +
                "GROUP BY p.id " +
                "ORDER BY totalProfit DESC, p.id ASC";
    }

    public List<Object[]> getTopThreeMostPopularProducts() {
        String hql = createPopularProductsHQL();
        Query<Object[]> query = this.getCurrentSession().createQuery(hql, Object[].class);
        query.setMaxResults(3);
        return query.getResultList();
    }

    private String createPopularProductsHQL() {
        return "SELECT p.productId, p.name, SUM(oi.quantity) AS totalQuantity " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "JOIN oi.product p " +
                "WHERE o.orderStatus <> 'Processing' AND o.orderStatus <> 'Canceled' " +
                "GROUP BY p.id " +
                "ORDER BY totalQuantity DESC, p.id ASC";
    }

    public long getTotalSoldItems() {
        String hql = createTotalSoldItemsHQL();
        Query<Long> query = this.getCurrentSession().createQuery(hql, Long.class);
        return query.uniqueResult();
    }

    private String createTotalSoldItemsHQL() {
        return "SELECT SUM(oi.quantity) " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "WHERE o.orderStatus <> 'Processing' AND o.orderStatus <> 'Canceled'";
    }



}