package org.example.utility;

import org.example.entity.Payment;
import org.example.events.OrderCreatedEvent;

public class OrderCreatedEventToPaymentMapper {

    public static Payment toPayment(OrderCreatedEvent event) {

        return Payment.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())         // <-- NOW INCLUDED
                .amount(event.getAmount())
                .paymentMethod("UPI")              // default
                .status(event.getOrderStatus())    // CREATED
                .transactionId("TXN-" + System.currentTimeMillis())
                .build();
    }
}
