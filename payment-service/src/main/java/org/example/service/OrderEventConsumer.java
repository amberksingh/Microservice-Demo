package org.example.service;

import org.example.entity.Payment;
import org.example.enums.PaymentStatus;
import org.example.events.OrderCreatedEvent;
import org.example.events.PaymentCompletedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.repo.PaymentJpaRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentJpaRepo jpaRepo;

    public OrderEventConsumer(KafkaTemplate<String, Object> kafkaTemplate, PaymentJpaRepo jpaRepo) {
        this.kafkaTemplate = kafkaTemplate;
        this.jpaRepo = jpaRepo;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void consumeOrder(OrderCreatedEvent event) {
        System.out.println("PaymentService: processing " + event);

        double balance = 50000;  // 🔥 Hardcoded for now
        if (event.getAmount() <= balance) {
            PaymentCompletedEvent success = PaymentCompletedEvent.builder()
                    .orderId(event.getOrderId())
                    .amount(event.getAmount())
                    .transactionId("TXN-" + System.currentTimeMillis())
                    .paymentStatus(PaymentStatus.SUCCESS.toString())
                    .build();
            System.out.println("Payment success for id :" + event.getOrderId());
            Payment saved1 = jpaRepo.save(Payment.builder()
                    .orderId(success.getOrderId())
                    .amount(success.getAmount())
                    .transactionId(success.getTransactionId())
                    .status("SUCCESS")
                    .build());
            System.out.println("💰 Payment created: " + saved1);
            kafkaTemplate.send("payment-events", success);
        } else {
            PaymentFailedEvent failed = PaymentFailedEvent.builder()
                    .orderId(event.getOrderId())
                    .amount(event.getAmount())
                    .reason("INSUFFICIENT BALANCE")
                    .paymentStatus(PaymentStatus.FAILED.toString())
                    .build();
            System.out.println("Payment failed for id :" + event.getOrderId());
            Payment saved2 = jpaRepo.save(Payment.builder()
                    .orderId(failed.getOrderId())
                    .amount(failed.getAmount())
                    //.transactionId(success.getTransactionId())
                    .status("FAILED")
                    .build());
            System.out.println("💰 Payment created: " + saved2);
            kafkaTemplate.send("payment-events", failed);
        }
    }
}
