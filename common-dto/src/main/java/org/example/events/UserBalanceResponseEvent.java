package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBalanceResponseEvent implements Serializable {

    private Long orderId;       // maps result to order
    private Long userId;        // same user
    private Double amount;      // requested amount
    private boolean success;    // TRUE = deduction done
    private String message;     // "Deducted", "Insufficient Balance", "User not found"
    private String paymentStatus;
}
