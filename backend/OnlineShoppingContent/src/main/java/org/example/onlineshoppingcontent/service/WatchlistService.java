package org.example.onlineshoppingcontent.service;

import org.example.onlineshoppingcontent.dao.ProductDao;
import org.example.onlineshoppingcontent.dao.UserDao;
import org.example.onlineshoppingcontent.exception.NoResultException;
import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.model.User;
import org.example.onlineshoppingcontent.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    private final UserDao userDao;
    private final ProductDao productDao;
    private final EntityManager entityManager;

    @Autowired
    public WatchlistService (UserDao userDao, ProductDao productDao, EntityManager entityManager) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.entityManager = entityManager;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof String)) {
            return null;
        }
        String username = (String) authentication.getPrincipal();
        return userDao.getUserByUsername(username);
    }

    // add transactional to avoid user cannot be recognized on different thread
    @Transactional
    public List<Object[]> getAllInStockProductsInWatchlist() {
        User user = getCurrentUser();
        List<Long> productIds = user.getWatchlist().stream()
                .map(Product::getProductId)
                .collect(Collectors.toList());

        List<Object[]> result = entityManager.createQuery(
                        "SELECT p.productId, p.description, p.name, p.retailPrice " +
                                "FROM Product p WHERE p.productId IN :productIds AND p.quantity > 0", Object[].class)
                .setParameter("productIds", productIds)
                .getResultList();

        return result;
    }

    @Transactional
    public void addToWatchlist(Long productId) throws NoResultException {
        Product product = productDao.getProductById(productId);
        User user = getCurrentUser();
        Set<Long> productIds =  user.getWatchlist().stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());
        if (productIds.contains(product.getProductId())) {
            throw new NoResultException("Product is already in the watchlist");
        }
        user.getWatchlist().add(product);

        userDao.save(user);
    }

    @Transactional
    public void removeFromWatchlist(Long productId) throws NoResultException {
        User user = getCurrentUser();
        Set<Product> watchlist = user.getWatchlist();
        Product product = productDao.getProductById(productId);
        if (!watchlist.contains(product)) {
            throw new NoResultException("Product is not in the watchlist");
        }
        user.getWatchlist().remove(product);

        userDao.saveUser(user);
    }
}