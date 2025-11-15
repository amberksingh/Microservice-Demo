package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.events.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.ORDER_EVENTS_TOPIC;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {
        kafkaTemplate.send(ORDER_EVENTS_TOPIC, event);
        System.out.println("📤 Sent OrderCreatedEvent → " + event);
    }
}