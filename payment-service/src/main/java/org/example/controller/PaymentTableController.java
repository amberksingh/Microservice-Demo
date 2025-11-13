package org.example.controller;

import org.example.PaymentRequest;
import org.example.entity.Payment;
import org.example.repo.PaymentJdbcRepo;
import org.example.repo.PaymentJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-table")
public class PaymentTableController {

    PaymentRequest paymentRequest;

    @Autowired
    PaymentJpaRepo jpaRepo;

    @Autowired
    PaymentJdbcRepo jdbcRepo;

    @GetMapping("/findById/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        Payment payment = jpaRepo.findById(id).orElseThrow(() -> new RuntimeException("payment not found for id : " + id));
        System.out.println("payment : findById : " + payment);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/findAllPayments")
    public ResponseEntity<List<Payment>> findAllPayments() {
        List<Payment> ordersRaw = jdbcRepo.findAllPayments();
        System.out.println("all payments : ");
        ordersRaw.forEach(System.out::println);
        return ResponseEntity.ok(ordersRaw);
    }

    @PostMapping("/addPayment")
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment) {
        Payment save = jpaRepo.save(payment);
        System.out.println("payment added: " + save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PostMapping("/addPayments")
    public ResponseEntity<List<Payment>> addMultiplePayments(@RequestBody List<Payment> payments) {
        List<Payment> save = jpaRepo.saveAll(payments);
        System.out.println("payments added: " + save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PostMapping("/paymentWithOrderDetailsForEntity")
    public ResponseEntity<String> addPaymentForEntity(@RequestBody PaymentRequest request) {

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus());

        Payment saved = jpaRepo.save(payment);
        System.out.println("💰 Payment created: " + saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Payment successful for Order ID: " + saved.getOrderId());
    }

    @PostMapping("/paymentWithOrderDetailsForObject")
    public String addPaymentForObject(@RequestBody PaymentRequest request) {

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setTransactionId(request.getTransactionId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus());

        Payment saved = jpaRepo.save(payment);
        System.out.println("💰 Payment created: " + saved);

        return "Payment successful for Order ID: " + saved.getOrderId();
    }

    @GetMapping("/fetchOrderByPaymentMethodForObject/{paymentMethod}")
    public List<PaymentRequest> fetchOrderByPaymentMethodForObjet(@PathVariable String paymentMethod) {
        System.out.println("Payment service fetchOrderByPaymentMethodForObject :");
        List<Payment> byPaymentMethod = jpaRepo.findByPaymentMethodIgnoreCase(paymentMethod);
        //byPaymentMethod.forEach(System.out::println);

        return byPaymentMethod.stream()
                .map(payment -> PaymentRequest.builder()
                        .paymentMethod(payment.getPaymentMethod())
                        .orderId(payment.getOrderId())
                        .transactionId(payment.getTransactionId())
                        .amount(payment.getAmount())
                        .build()
                )
                .toList();
        // Spring auto converts List<Payment> → JSON array
    }

    @GetMapping("/fetchOrderByPaymentMethodForEntity/{paymentMethod}")
    public ResponseEntity<List<PaymentRequest>> fetchOrderByPaymentMethodForEntity(@PathVariable String paymentMethod) {
        System.out.println("Payment service fetchOrderByPaymentMethodForEntity :");
        List<Payment> byPaymentMethod = jpaRepo.findByPaymentMethodIgnoreCase(paymentMethod);
        //byPaymentMethod.forEach(System.out::println);

        List<PaymentRequest> list = byPaymentMethod.stream()
                .map(payment -> PaymentRequest.builder()
                        .paymentMethod(payment.getPaymentMethod())
                        .orderId(payment.getOrderId())
                        .transactionId(payment.getTransactionId())
                        .amount(payment.getAmount())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(list);
        // Spring auto converts List<Payment> → JSON array
    }


}
