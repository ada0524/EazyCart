package org.example.onlineshoppingcontent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="order_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "purchased_price", nullable = false)
    private double purchasedPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "wholesale_price", nullable = false)
    private double wholesalePrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    public OrderItem(double retailPrice, int quantity, double wholesalePrice, Order order, Product product) {
        this.purchasedPrice = retailPrice;
        this.quantity = quantity;
        this.wholesalePrice = wholesalePrice;
        this.order = order;
        this.product = product;
    }
}

