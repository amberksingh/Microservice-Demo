package org.example.service;

import org.example.events.PaymentCompletedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class PaymentEventConsumer {

        @Autowired
        OrderJpaRepo repo;

        @KafkaListener(topics = "payment-events", groupId = "order-group")
        public void consume(PaymentCompletedEvent event) {
            System.out.println("event = " + event);
            System.out.println("Payment SUCCESS for " + event.getOrderId());

            repo.updateStatus(event.getOrderId(), "SUCCESS");
        }

        @KafkaListener(topics = "payment-events", groupId = "order-group")
        public void consume(PaymentFailedEvent event) {
            System.out.println("event = " + event);
            System.out.println("Payment FAILED for " + event.getOrderId());
            repo.updateStatus(event.getOrderId(), "FAILED");
        }

//        if (event instanceof PaymentCompletedEvent success) {
//            repo.updateStatus(success.getOrderId(), OrderStatus.PAYMENT_SUCCESS.name());
//            System.out.println("✔ Order updated to PAYMENT_SUCCESS");
//        }
//        else if (event instanceof PaymentFailedEvent fail) {
//            repo.updateStatus(fail.getOrderId(), OrderStatus.PAYMENT_FAILED.name());
//            System.out.println("❌ Order updated to PAYMENT_FAILED");
//        }

}
