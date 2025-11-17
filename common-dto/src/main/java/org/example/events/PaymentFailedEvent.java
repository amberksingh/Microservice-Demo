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
public class PaymentFailedEvent implements Serializable {
    private Long orderId;
    private Long userId;
    private Double amount;
    private String reason;
    private String transactionId;
    private String paymentStatus;  // FAILED
}
