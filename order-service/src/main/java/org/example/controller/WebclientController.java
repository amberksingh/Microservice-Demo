package org.example.controller;

import org.example.PaymentRequest;
import org.example.entity.Order;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import static org.example.config.Constants.BASE_URL_PAYMENT;
import static org.example.enums.OrderStatus.CREATED;

@RestController
@RequestMapping("/webClient")
public class WebclientController {

    @Autowired
    OrderJpaRepo jpaRepo;

//    public String callPaymentBlocking(PaymentRequest request) {
//
//        WebClient webClient = WebClient.builder()
//                .baseUrl(BASE_URL_PAYMENT)
//                .build();
//        return webClient
//                .post()
//                .uri("/payment-table/addPaymentWebClient")
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();  // <-- BLOCKS thread
//    }

    @PostMapping("/addOrderWithPaymentBlocking")
    public ResponseEntity<String> addOrderWithPaymentBlocking(@RequestBody Order order) {

        System.out.println("webclient BLOCKING call demo..");
        Order save = jpaRepo.save(order);
        System.out.println("order saved in order db:" + save);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod("UPI");
        paymentRequest.setStatus(CREATED.toString());
        paymentRequest.setOrderId(save.getId());
        paymentRequest.setAmount(save.getAmount());
        paymentRequest.setTransactionId("TXN-" + System.currentTimeMillis());

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL_PAYMENT)
                .build();
        String response = webClient
                .post()
                .uri("/payment-table/addPaymentWebClient")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // <-- BLOCKS thread

        System.out.println("💳 Payment Service Response: " + response);

        save.setStatus("PAID");
        jpaRepo.save(save);
        System.out.println("status updated to PAID for id:" + save.getId());

        return ResponseEntity.ok(response);

    }

    @PostMapping("/addOrderWithPaymentAsync")
    public ResponseEntity<String> addOrderWithPaymentAsync(@RequestBody Order order) {
        System.out.println("webclient NON-BLOCKING subscribe call demo..");
        Order save = jpaRepo.save(order);
        System.out.println("order saved in order db:" + save);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod("UPI");
        paymentRequest.setStatus(CREATED.toString());
        paymentRequest.setOrderId(save.getId());
        paymentRequest.setAmount(save.getAmount());
        paymentRequest.setTransactionId("TXN-" + System.currentTimeMillis());

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL_PAYMENT)
                .build();
        webClient.post()
                .uri("/payment-table/addPaymentWebClient")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    System.out.println("💳 Async subscribe Payment Response: " + response);
                    save.setStatus("PAID");
                    jpaRepo.save(save);
                    System.out.println("✔ Order updated to PAID (async): " + save.getId());
                });
        // Step 4: Return immediately
        return ResponseEntity.ok("Order placed! Payment processing asynchronously… Order ID: " + save.getId());
    }

    @PostMapping("/addOrderWithPaymentAsyncMap")
    public Mono<String> addOrderWithPaymentAsyncMap(@RequestBody Order order) {
        System.out.println("webclient NON-BLOCKING map call demo..");
        Order save = jpaRepo.save(order);
        System.out.println("order saved in order db:" + save);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod("UPI");
        paymentRequest.setStatus(CREATED.toString());
        paymentRequest.setOrderId(save.getId());
        paymentRequest.setAmount(save.getAmount());
        paymentRequest.setTransactionId("TXN-" + System.currentTimeMillis());

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL_PAYMENT)
                .build();
        return webClient.post()
                .uri("/payment-table/addPaymentWebClient")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    System.out.println("💳 Async map Payment Response: " + response);
                    save.setStatus("PAID");
                    jpaRepo.save(save);
                    System.out.println("✔ Order updated to PAID (async): " + save.getId());
                    return response;
                });
                //.subscribe();

        // Step 4: Return immediately
        //return ResponseEntity.ok("Order placed! Payment processing asynchronously… Order ID: " + save.getId());
    }
}