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
public class UserBalanceRequestEvent implements Serializable {

    private Long orderId;       // for mapping payment to order
    private Long userId;        // whose balance to check
    private Double amount;      // amount requested for deduction

    // Optional: for debugging, tracing, logs
    private String paymentMethod;
}
