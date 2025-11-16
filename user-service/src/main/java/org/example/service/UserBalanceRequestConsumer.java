package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.UserJdbcRepo;
import org.example.entity.User;
import org.example.events.OrderCreatedEvent;
import org.example.events.UserBalanceRequestEvent;
import org.example.events.UserBalanceResponseEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.config.Constants.*;

@Slf4j
@Service
@KafkaListener(topics = USER_BALANCE_REQUESTS_TOPIC, groupId = USER_GROUP)
public class UserBalanceRequestConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BalanceCheckService balanceCheckService;

    public UserBalanceRequestConsumer(KafkaTemplate<String, Object> kafkaTemplate, BalanceCheckService balanceCheckService) {
        this.kafkaTemplate = kafkaTemplate;
        this.balanceCheckService = balanceCheckService;
    }

    @KafkaHandler
    public void consumeBalanceCheckRequest(UserBalanceRequestEvent balanceRequestEvent) {

        boolean ok = balanceCheckService.deductBalance(balanceRequestEvent.getUserId(), balanceRequestEvent.getAmount());
        UserBalanceResponseEvent balanceResponseEvent = new UserBalanceResponseEvent(
                balanceRequestEvent.getOrderId(),
                balanceRequestEvent.getUserId(),
                balanceRequestEvent.getAmount(),
                ok,
                ok ? "Balance deducted" : "Insufficient balance"
        );
        kafkaTemplate.send(USER_BALANCE_RESPONSES_TOPIC, balanceResponseEvent);
        log.info("User-Service sent balance response: {}", balanceResponseEvent);

    }

}
