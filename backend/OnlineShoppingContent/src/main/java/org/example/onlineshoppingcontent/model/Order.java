package org.example.onlineshoppingcontent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "date_placed", nullable = false)
    private LocalDateTime datePlaced;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OrderItem> orderItems = new HashSet<>();

    // override hashcode to avoid stack overflow
    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order other = (Order) obj;
        return Objects.equals(orderId, other.orderId);
    }

}

