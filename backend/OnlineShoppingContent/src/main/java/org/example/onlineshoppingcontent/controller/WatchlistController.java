package org.example.onlineshoppingcontent.controller;

import org.example.onlineshoppingcontent.exception.NoResultException;
import org.example.onlineshoppingcontent.model.Product;
import org.example.onlineshoppingcontent.service.ProductService;
import org.example.onlineshoppingcontent.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {


    private final WatchlistService watchlistService;
    private final ProductService productService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, ProductService productService) {
        this.watchlistService = watchlistService;
        this.productService = productService;
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<Object[]>> getAllInStockProductsInWatchlist() {
        // (return object to resolve lazy loading problem)
        List<Object[]> result = watchlistService.getAllInStockProductsInWatchlist();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<String> addToWatchlist(@PathVariable Long productId) {
        try {
            watchlistService.addToWatchlist(productId);
            return ResponseEntity.ok("Product successfully added to the watchlist.");
        } catch (NoResultException e) {
            throw new NoResultException("The product is already in the watchlist.");
        }
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable Long productId) {
        try {
            watchlistService.removeFromWatchlist(productId);
            return ResponseEntity.ok("Product successfully removed from the watchlist.");
        } catch (NoResultException e) {
            throw new NoResultException("No product found in the watchlist.");
        }
    }
}

