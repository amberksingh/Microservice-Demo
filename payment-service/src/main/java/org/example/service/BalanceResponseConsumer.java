package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Payment;
import org.example.events.PaymentCompletedEvent;
import org.example.events.UserBalanceResponseEvent;
import org.example.repo.PaymentJpaRepo;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.*;

@Service
@Slf4j
@KafkaListener(topics = USER_BALANCE_RESPONSES, groupId = PAYMENT_GROUP)
public class BalanceResponseConsumer {

    private final PaymentJpaRepo repo;

    public BalanceResponseConsumer(PaymentJpaRepo repo) {
        this.repo = repo;
    }

    @KafkaHandler
    public void handle(UserBalanceResponseEvent userBalanceResponseEvent) {
        log.info("Callback triggered on payment from User");
        Payment payment = repo.findByOrderId(userBalanceResponseEvent.getOrderId());
        if (userBalanceResponseEvent.isSuccess()) {
            log.info("Payment success → continue workflow");
            payment.setStatus("SUCCESS");
            repo.save(payment);
            log.info("payment status updated to SUCCESS");
        } else {
            log.warn("Payment failed → {}", userBalanceResponseEvent.getMessage());
            payment.setStatus("FAIL");
            repo.save(payment);
            log.info("payment status updated to FAIL");
        }
    }
}
