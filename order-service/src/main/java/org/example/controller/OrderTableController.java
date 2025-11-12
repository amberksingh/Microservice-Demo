package org.example.controller;

import org.example.PaymentRequest;
import org.example.entity.Order;
import org.example.repo.OrderJdbcRepo;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.example.config.Constants.BASE_URL_PAYMENT;

@RestController
@RequestMapping("/order-table")
public class OrderTableController {

    PaymentRequest paymentRequest;

    @Autowired
    OrderJpaRepo jpaRepo;

    @Autowired
    OrderJdbcRepo jdbcRepo;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/findById/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order order = jpaRepo.findById(id).orElseThrow(() -> new RuntimeException("order not found for id : " + id));
        System.out.println("order : findById : " + order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/findAllOrders")
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> ordersRaw = jdbcRepo.findOrdersRaw();
        System.out.println("ordersRaw : ");
        ordersRaw.forEach(System.out::println);
        return ResponseEntity.ok(ordersRaw);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        Order save = jpaRepo.save(order);
        System.out.println("order added: " + save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PostMapping("/addMultipleOrders")
    public ResponseEntity<List<Order>> addMultipleOrders(@RequestBody List<Order> orders) {
        List<Order> save = jpaRepo.saveAll(orders);
        System.out.println("orders added: " + save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PostMapping("/addOrderWithPayment")
    public ResponseEntity<Order> addOrderWithPayment(@RequestBody Order order) {
        // Step 1: Save order
        Order savedOrder = jpaRepo.save(order);
        System.out.println("✅ Order saved: " + savedOrder);

        // Step 2: Build payment request DTO
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setTransactionId("TXN-" + System.currentTimeMillis());
        paymentRequest.setAmount(savedOrder.getAmount());
        paymentRequest.setPaymentMethod("UPI");
        paymentRequest.setStatus("SUCCESS");

        // Step 3: Call payment-service
        String paymentServiceUrl = BASE_URL_PAYMENT + "/payment-table/paymentWithOrderDetails";
        ResponseEntity<String> paymentResponse =
                restTemplate.postForEntity(paymentServiceUrl, new HttpEntity<>(paymentRequest), String.class);

        //String created = restTemplate.postForObject(paymentServiceUrl, paymentRequest, String.class);

        System.out.println("💳 Payment Service Response: " + paymentResponse.getBody());

        // Step 4: Optionally update order status
        savedOrder.setStatus("PAID");
        jpaRepo.save(savedOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}
