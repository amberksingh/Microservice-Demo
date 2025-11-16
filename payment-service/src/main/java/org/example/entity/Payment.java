package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    private Double amount;

    @Column(name = "payment_method")
    private String paymentMethod; // CARD, UPI, NETBANKING

    private String status; // SUCCESS, FAILED, PENDING

    //@CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // For manual override if user sends it
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            System.out.println("createdAt is null. Setting in code");
            this.createdAt = LocalDateTime.now();
        }
    }
}

