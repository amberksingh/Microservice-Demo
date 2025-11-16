package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Payment;
import org.example.enums.PaymentStatus;
import org.example.events.OrderCreatedEvent;
import org.example.events.PaymentCompletedEvent;
import org.example.events.PaymentFailedEvent;
import org.example.repo.PaymentJpaRepo;
import org.example.utility.OrderCreatedEventToPaymentMapper;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.config.Constants.*;

@Service
@Slf4j
@KafkaListener(topics = ORDER_EVENTS_TOPIC, groupId = PAYMENT_GROUP)
public class OrderEventConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentJpaRepo jpaRepo;
    private final BalanceCheckProducer balanceCheckProducer;

    public OrderEventConsumer(KafkaTemplate<String, Object> kafkaTemplate, PaymentJpaRepo jpaRepo, BalanceCheckProducer balanceCheckProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.jpaRepo = jpaRepo;
        this.balanceCheckProducer = balanceCheckProducer;
    }

    //@KafkaListener(topics = "order-events", groupId = "payment-group")
    @KafkaHandler
    public void consumeOrder(OrderCreatedEvent orderCreatedEvent) {
        //System.out.println("PaymentService: processing " + event);
        Payment payment = OrderCreatedEventToPaymentMapper.toPayment(orderCreatedEvent);
        jpaRepo.save(payment);
        log.info("persisting payment details intermittently in payment-service {}", payment);

        //ACTUAL FLOW=====USER SERVICE
        log.info("ACTUAL BALANCE CHECK FLOW ===== USER SERVICE ENTERS THE CHAT");
        balanceCheckProducer.requestBalance(orderCreatedEvent);


        /*double balance = 50000;  // 🔥 Hardcoded for now
        if (event.getAmount() <= balance) {

            PaymentCompletedEvent success = new PaymentCompletedEvent();
            success.setOrderId(event.getOrderId());
            success.setAmount(event.getAmount());
            success.setTransactionId("TXN-" + System.currentTimeMillis());
            success.setPaymentStatus(PaymentStatus.SUCCESS.toString());

//            PaymentCompletedEvent success = PaymentCompletedEvent.builder()
//                    .orderId(event.getOrderId())
//                    .amount(event.getAmount())
//                    .transactionId("TXN-" + System.currentTimeMillis())
//                    .paymentStatus(PaymentStatus.SUCCESS.toString())
//                    .build();
            System.out.println("Payment success for id :" + event.getOrderId());
            Payment saved1 = jpaRepo.save(Payment.builder()
                    .orderId(success.getOrderId())
                    .amount(success.getAmount())
                    .transactionId(success.getTransactionId())
                    .status("SUCCESS")
                    .build());
            System.out.println("💰 Payment created: " + saved1);
            //kafkaTemplate.send("payment-events", success);
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, success);
        } else {

            PaymentFailedEvent failed = new PaymentFailedEvent();
            failed.setOrderId(event.getOrderId());
            failed.setAmount(event.getAmount());
            failed.setReason("INSUFFICIENT BALANCE");
            failed.setPaymentStatus(PaymentStatus.FAILED.toString());

//            PaymentFailedEvent failed = PaymentFailedEvent.builder()
//                    .orderId(event.getOrderId())
//                    .amount(event.getAmount())
//                    .reason("INSUFFICIENT BALANCE")
//                    .paymentStatus(PaymentStatus.FAILED.toString())
//                    .build();
            System.out.println("Payment failed for id :" + event.getOrderId());
            Payment saved2 = jpaRepo.save(Payment.builder()
                    .orderId(failed.getOrderId())
                    .amount(failed.getAmount())
                    .transactionId("FAIL-" + System.currentTimeMillis())
                    .status("FAILED")
                    .build());
            System.out.println("💰 Payment created: " + saved2);
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, failed);
        }*/
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object obj) {
        System.out.println("⚠ Unknown event received in payment: " + obj);
    }
}
