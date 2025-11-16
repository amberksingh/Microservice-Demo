package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderCreatedEvent;
import org.example.events.UserBalanceRequestEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.USER_BALANCE_REQUESTS;

@Service
@Slf4j
public class BalanceCheckProducer {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BalanceCheckProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //@Value("${kafka.topics.user-balance-requests}")
    //private String reqTopic;

    public void requestBalance(OrderCreatedEvent orderCreatedEvent) {

        UserBalanceRequestEvent balanceRequestEvent = new UserBalanceRequestEvent();
        balanceRequestEvent.setAmount(orderCreatedEvent.getAmount());
        balanceRequestEvent.setUserId(orderCreatedEvent.getUserId());
        balanceRequestEvent.setOrderId(orderCreatedEvent.getOrderId());
        balanceRequestEvent.setPaymentMethod("UPI");

        log.info("sending balanceRequestEvent to user-service {}", balanceRequestEvent);
        kafkaTemplate.send(USER_BALANCE_REQUESTS, balanceRequestEvent);

    }
}
