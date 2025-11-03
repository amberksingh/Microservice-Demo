package org.example.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.example.config.Constants.BASE_URL_PAYMENT;
import static org.example.config.Constants.ORDER_SERVICE;

@RestController
@RequestMapping("order-service")
public class OrderController {

    int attempt = 1;

    @Autowired
    OrderClient orderClient;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/dummy")
    public String dummyMessage() {
        return "Hello..dummy message from order-service";
    }

//    @GetMapping("/payment/{userId}")
//    //@Retry(name = "order-service", fallbackMethod = "paymentFallback")
//    public String getPaymentStatusForUser(@PathVariable("userId") String userId) {
//        System.out.println("Attempt in order-service: "+attempt);
//        attempt++;
//        System.out.println("Feign : order-service : trying payment for userId : " + userId);
//        return orderClient.getPaymentStatusForUser(userId);
//        //return "order-service : payment success for userId : " + userId;
//    }

    //this is called as soon as payment-service is down for below @CircuitBreaker
    //this is called as soon as payment-service is suppose waiting for 6000ms for below @CircuitBreaker
    public String paymentFallback(String userId, Throwable t) {
        System.out.println("Inside fallback method for @CircuitBreaker : " + t.getMessage());
        return "paymentFallback : Payment-service TIMED OUT for userId: " + userId;
    }

    @GetMapping("/payment/{userId}")
    @CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "paymentFallback")
    public String getPaymentStatusForUserRestTemplate(@PathVariable String userId) {
        //try {
            System.out.println("RestTemplate way...");
            String url = BASE_URL_PAYMENT + "/payment-service/payment/" + userId;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        /*} catch (Exception e) {
            System.out.println("Exception while calling payment-service : " + e.getMessage());
            return "RestTemplate catch : Payment-service TIMED OUT or failed for userId: " + userId;
        }*/
    }

}
