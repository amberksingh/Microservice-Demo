package org.example.controller;

import org.example.entity.Order;
import org.example.enums.OrderStatus;
import org.example.events.OrderCreatedEvent;
import org.example.repo.OrderJpaRepo;
import org.example.service.OrderEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaVersionOrderController {

    @Autowired
    OrderJpaRepo jpaRepo;

    @Autowired
    OrderEventProducer orderEventProducer;

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        Order saved = jpaRepo.save(order);
        System.out.println("order added to db: " + saved);
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .amount(saved.getAmount())
                .productName(saved.getProductName())
                .userId(order.getUserId())
                .orderStatus(OrderStatus.CREATED.toString())
                .build();
        System.out.println("OrderCreatedEvent : "+event);
        orderEventProducer.sendOrderCreatedEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
