package org.example.onlineshoppingcontent.service;

import org.example.onlineshoppingcontent.dao.ProductDao;
import org.example.onlineshoppingcontent.dao.UserDao;
import org.example.onlineshoppingcontent.dto.admin.request.ProductRequest;
import org.example.onlineshoppingcontent.model.OrderItem;
import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.model.User;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public ProductService(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof String)) {
            return null;
        }
        String username = (String) authentication.getPrincipal();
        return userDao.getUserByUsername(username);
    }

    public List<Map<String, Object>> getAllUserReadProducts() {
        List<Product> allProducts = productDao.getAllProducts();

        List<Map<String, Object>> filteredProducts = allProducts.stream()
                .filter(product -> product.getQuantity() > 0)
                .map(this::mapToAllProductMap)
                .collect(Collectors.toList());

        return filteredProducts;
    }

    public List<Map<String, Object>> getAllAdminReadProducts() {
        List<Product> allProducts = productDao.getAllProducts();

        List<Map<String, Object>> filteredProducts = allProducts.stream()
                .map(this::mapToAllProductMap)
                .collect(Collectors.toList());

        return filteredProducts;
    }

    private Map<String, Object> mapToAllProductMap(Product product) {
        Map<String, Object> productMap = new LinkedHashMap<>();
        productMap.put("productId", product.getProductId());
        productMap.put("name", product.getName());

        return productMap;
    }

    public Map<String, Object> getUserReadProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return mapToUserProductDetailMap(product);
    }

    private Map<String, Object> mapToUserProductDetailMap(Product product) {
        Map<String, Object> productMap = new LinkedHashMap<>();
        productMap.put("productId", product.getProductId());
        productMap.put("description", product.getDescription());
        productMap.put("name", product.getName());
        productMap.put("retailPrice", product.getRetailPrice());

        return productMap;
    }

    public  Map<String, Object> getAdminReadProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return mapToAdminProductDetailMap(product);
    }

    private Map<String, Object> mapToAdminProductDetailMap(Product product) {
        Map<String, Object> productMap = new LinkedHashMap<>();
        productMap.put("productId", product.getProductId());
        productMap.put("description", product.getDescription());
        productMap.put("name", product.getName());
        productMap.put("quantity", product.getQuantity());
        productMap.put("retailPrice", product.getRetailPrice());
        productMap.put("wholesalePrice", product.getWholesalePrice());

        return productMap;
    }


    public void addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setWholesalePrice(productRequest.getWholesalePrice());
        product.setRetailPrice(productRequest.getRetailPrice());
        product.setQuantity(productRequest.getQuantity());

        productDao.saveProduct(product);
    }

    public List<Object[]> getTopThreeMostFrequentlyPurchasedItems() {
        Long userId = getCurrentUser().getUserId();
        return productDao.getTopThreeMostFrequentlyPurchasedItems(userId);
    }

    public List<Object[]> getTopThreeMostRecentlyPurchasedItems() {
        Long userId = getCurrentUser().getUserId();
        return productDao.getTopThreeMostRecentlyPurchasedItems(userId);
    }

    public List<Object[]> getTopThreeMostProfitableItems() {
        return productDao.getTopThreeMostProfitableItems();
    }

    public List<Object[]> getTopThreeMostPopularProducts() {
        return productDao.getTopThreeMostPopularProducts();
    }

}
