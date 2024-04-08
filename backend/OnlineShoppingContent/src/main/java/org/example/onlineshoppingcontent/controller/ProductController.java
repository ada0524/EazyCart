package org.example.onlineshoppingcontent.controller;

import org.example.onlineshoppingcontent.dto.admin.request.ProductRequest;
import org.example.onlineshoppingcontent.model.Product;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("user_read"))) {
            List<Map<String, Object>> userProducts = productService.getAllUserReadProducts();
            return new ResponseEntity<>(userProducts, HttpStatus.OK);
        } else if (authorities.contains(new SimpleGrantedAuthority("admin_read"))) {
            List<Map<String, Object>> adminProducts = productService.getAllAdminReadProducts();
            return new ResponseEntity<>(adminProducts, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("user_read"))) {
            Map<String, Object> userProduct = productService.getUserReadProductById(productId);
            return new ResponseEntity<>(userProduct, HttpStatus.OK);
        } else if (authorities.contains(new SimpleGrantedAuthority("admin_read"))) {
            Map<String, Object> adminProduct = productService.getAdminReadProductById(productId);
            return new ResponseEntity<>(adminProduct, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @PostMapping("")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.addProduct(productRequest);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.addProduct(productRequest);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/frequent/3")
    public ResponseEntity<List<Object[]>> getTopThreeMostFrequentlyPurchasedItems() {
        List<Object[]> purchasedItems = productService.getTopThreeMostFrequentlyPurchasedItems();
        return  new ResponseEntity<>(purchasedItems, HttpStatus.OK);
    }

    @GetMapping("/recent/3")
    public ResponseEntity<List<Object[]>> getTopThreeMostRecentlyPurchasedItems() {
        List<Object[]> purchasedItems = productService.getTopThreeMostRecentlyPurchasedItems();
        return new ResponseEntity<>(purchasedItems, HttpStatus.OK);
    }

    @GetMapping("/profit/3")
    public ResponseEntity<List<Object[]>> getTopThreeMostProfitableItem() {
        List<Object[]> profitableItems = productService.getTopThreeMostProfitableItems();
        return new ResponseEntity<>(profitableItems, HttpStatus.OK);
    }

    @GetMapping("/popular/3")
    public ResponseEntity<List<Object[]>> getTopThreeMostPopularProducts() {
        List<Object[]> popularProducts = productService.getTopThreeMostPopularProducts();
        return new ResponseEntity<>(popularProducts, HttpStatus.OK);
    }


}
