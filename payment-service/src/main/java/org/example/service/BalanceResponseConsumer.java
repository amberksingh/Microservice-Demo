package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Payment;
import org.example.enums.PaymentStatus;
import org.example.events.PaymentCompletedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.events.UserBalanceResponseEvent;
import org.example.repo.PaymentJpaRepo;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.*;

@Service
@Slf4j
@KafkaListener(topics = USER_BALANCE_RESPONSES, groupId = PAYMENT_GROUP)
public class BalanceResponseConsumer {

    private final PaymentJpaRepo repo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BalanceResponseConsumer(PaymentJpaRepo repo, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repo = repo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    public void handle(UserBalanceResponseEvent userBalanceResponseEvent) {
        log.info("Callback triggered on payment from User");
        //null issue
        Payment payment = repo.findByOrderId(userBalanceResponseEvent.getOrderId());
        if (userBalanceResponseEvent.isSuccess()) {

            log.info("Payment success → continue workflow");
            payment.setStatus("SUCCESS");
            repo.save(payment);
            log.info("payment status updated to SUCCESS in payment table");

            PaymentCompletedEvent successPaymentCompletedEvent = new PaymentCompletedEvent();
            successPaymentCompletedEvent.setOrderId(userBalanceResponseEvent.getOrderId());
            successPaymentCompletedEvent.setAmount(userBalanceResponseEvent.getAmount());
            successPaymentCompletedEvent.setTransactionId("TXN-" + System.currentTimeMillis());
            successPaymentCompletedEvent.setPaymentStatus(PaymentStatus.SUCCESS.toString());
            successPaymentCompletedEvent.setUserId(userBalanceResponseEvent.getUserId());

            log.info("sending back successPaymentCompletedEvent back to order-service : {}", successPaymentCompletedEvent);
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, successPaymentCompletedEvent);

        } else {
            log.warn("Payment failed → {}", userBalanceResponseEvent.getMessage());
            payment.setStatus("FAIL");
            repo.save(payment);
            log.info("payment status updated to FAIL in payment table");

            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent();
            paymentFailedEvent.setOrderId(userBalanceResponseEvent.getOrderId());
            paymentFailedEvent.setAmount(userBalanceResponseEvent.getAmount());
            paymentFailedEvent.setTransactionId("TXN-" + System.currentTimeMillis());
            paymentFailedEvent.setPaymentStatus(userBalanceResponseEvent.getPaymentStatus());
            paymentFailedEvent.setUserId(userBalanceResponseEvent.getUserId());
            paymentFailedEvent.setReason(userBalanceResponseEvent.getMessage());

            log.info("sending back paymentFailedEvent back to order-service : {}", paymentFailedEvent);
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, paymentFailedEvent);

        }
        //map from UserBalanceResponseEvent to PaymentCompletedEvent or PaymentFailedEvent
        //log.info("received UserBalanceResponseEvent from user-service {}", userBalanceResponseEvent);
        //kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, userBalanceResponseEvent);
    }
}
