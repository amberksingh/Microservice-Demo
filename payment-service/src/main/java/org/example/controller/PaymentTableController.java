package org.example.controller;

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
}
