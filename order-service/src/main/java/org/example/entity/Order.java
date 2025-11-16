package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // matches your SQL
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // aligns with AUTO_INCREMENT
    private Long id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "user_id")
    private Long userId;

    private Double amount;

    private String status; // CREATED, PAID, FAILED

    //@CreationTimestamp
    //@Column(name = "created_at", updatable = false)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Automatically fill before persist if null
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            System.out.println("createdAt is null. Setting in code");
            this.createdAt = LocalDateTime.now();
        }
    }

    public Order(String orderNumber, String productName, Double amount, String status) {
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
    }
    public Order(Long userId, String orderNumber, String productName, Double amount, String status) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
    }

    // getters and setters
}

