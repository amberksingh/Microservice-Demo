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
public class PaymentCompletedEvent implements Serializable {
    private Long orderId;
    private Double amount;
    private String transactionId;
    private String paymentStatus;  // SUCCESS
}
