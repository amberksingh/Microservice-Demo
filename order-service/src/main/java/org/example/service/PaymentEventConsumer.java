package org.example.service;

import org.example.events.PaymentCompletedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.ORDER_GROUP;
import static org.example.config.Constants.PAYMENT_EVENTS_TOPIC;

@Service
@KafkaListener(topics = PAYMENT_EVENTS_TOPIC, groupId = ORDER_GROUP)
public class PaymentEventConsumer {

    @Autowired
    OrderJpaRepo repo;

    @KafkaHandler
    public void handle(PaymentCompletedEvent event) {
        System.out.println("Callback triggered");
        System.out.println("✔ SUCCESS received: " + event);
        repo.updateStatus(event.getOrderId(), "SUCCESS");
    }

    @KafkaHandler
    public void handle(PaymentFailedEvent event) {
        System.out.println("Callback triggered");
        System.out.println("❌ FAILED received: " + event);
        repo.updateStatus(event.getOrderId(), "FAILED");
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object obj) {
        System.out.println("⚠ Unknown order event received: " + obj);
    }

//        @KafkaListener(topics = PAYMENT_EVENTS_TOPIC, groupId = "order-group")
//        public void consume(PaymentCompletedEvent event) {
//            System.out.println("Callback triggered");
//            System.out.println("event = " + event);
//            System.out.println("Payment SUCCESS for " + event.getOrderId());
//
//            repo.updateStatus(event.getOrderId(), "SUCCESS");
//        }
//
//        @KafkaListener(topics = PAYMENT_EVENTS_TOPIC, groupId = "order-group")
//        public void consume(PaymentFailedEvent event) {
//            System.out.println("Callback triggered");
//            System.out.println("event = " + event);
//            System.out.println("Payment FAILED for " + event.getOrderId());
//            repo.updateStatus(event.getOrderId(), "FAILED");
//        }


//    @KafkaListener(topics = "payment-events", groupId = "order-group")
//    public void consume(Object event) {
//        System.out.println("Callback triggered");
//        System.out.println("event log : "+event);
//        System.out.println("event log : "+event.getClass());
//        System.out.println("event log : "+event.toString());
//        if (event instanceof PaymentCompletedEvent success) {
//            System.out.println("✔ Order updated to SUCCESS");
//            repo.updateStatus(success.getOrderId(), "SUCCESS");
//        } else if (event instanceof PaymentFailedEvent fail) {
//            System.out.println("✔ Order updated to FAILED");
//            repo.updateStatus(fail.getOrderId(), "FAILED");
//        }
//    }

//        if (event instanceof PaymentCompletedEvent success) {
//            repo.updateStatus(success.getOrderId(), OrderStatus.PAYMENT_SUCCESS.name());
//            System.out.println("✔ Order updated to PAYMENT_SUCCESS");
//        }
//        else if (event instanceof PaymentFailedEvent fail) {
//            repo.updateStatus(fail.getOrderId(), OrderStatus.PAYMENT_FAILED.name());
//            System.out.println("❌ Order updated to PAYMENT_FAILED");
//        }

}
