package org.example.onlineshoppingcontent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name="product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "retail_price", nullable = false)
    private double retailPrice;

    @Column(name = "wholesale_price", nullable = false)
    private double wholesalePrice;

    @ManyToMany(mappedBy = "watchlist")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    public Product(String name, double retailPrice) {
        this.name = name;
        this.retailPrice = retailPrice;
    }

    // override hashcode to avoid stack overflow
    @Override
    public int hashCode() {
        return Objects.hash(productId, name, quantity, retailPrice, wholesalePrice);
    }
}
