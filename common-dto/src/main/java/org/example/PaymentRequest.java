package org.example;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private Long orderId;
    private String transactionId;
    private Double amount;
    private String paymentMethod;
    private String status;
}
