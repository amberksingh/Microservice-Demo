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

import java.util.Date;

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

    @GetMapping("/payment/{userId}")
    @Retry(name = ORDER_SERVICE, fallbackMethod = "feignFallback")
    public String getPaymentStatusForUser(@PathVariable("userId") String userId) {
        System.out.println("feign : Attempt in order-service: "+attempt);
        attempt++;
        System.out.println("Feign : order-service : trying payment for userId : " + userId);
        return orderClient.getPaymentStatusForUser(userId);
        //return "order-service : payment success for userId : " + userId;
    }

    public String feignFallback(String userId, Exception t) {//works with Exception and Throwable also
        System.out.println("feign fallback method for @CircuitBreaker : " + t.getMessage());
        return "feign paymentFallback : Payment-service TIMED OUT for userId: " + userId;
    }


    //this is called as soon as payment-service is down for below @CircuitBreaker
    //this is called as soon as payment-service is suppose waiting for 6000ms for below @CircuitBreaker
    public String paymentFallback(String userId, Throwable t) {
        System.out.println("Inside fallback method for @CircuitBreaker : " + t.getMessage());
        return "paymentFallback : Payment-service TIMED OUT for userId: " + userId;
    }

    //@GetMapping("/payment/{userId}")
    //fallback working for both  connection timeout and read timeout
    //@CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "paymentFallback")

    //1 : when payment is down and timeout configured in resttemplate, then retry happens and @CircuitBreaker missing
    //1.1 : when payment is down and timeout NOT configured in resttemplate, then also retry happens and @CircuitBreaker missing
    //2 : when payment is up but sleep for 9000ms and timeout configured in resttemplate, then retry happens and @CircuitBreaker missing
    //3 : when payment is up but sleep for 9000ms and timeout NOT configured in resttemplate, then retry does not happen.
    // and @CircuitBreaker missing. waits for 9s and gives response normally

    //4 when both @CircuitBreaker and @Retry configured and timeout NOT configured in resttemplate, then retry does not happen.
    //waits for 9s and gives response normally
    //5 : when both @CircuitBreaker and @Retry configured and timeout NOT configured in resttemplate, and payment is down,
    // then retry does not happen.directly fallback
    //6: when both @CircuitBreaker and @Retry configured and timeout NOT configured in resttemplate, and payment is waiting for 9s,
    //then retry does not happen. waits for 9s and gives response normally
    // 7: when both @CircuitBreaker and @Retry configured and timeout IS configured in resttemplate, and payment is down ,
    //then retry does not happen. fallback happens
    @Retry(name = ORDER_SERVICE, fallbackMethod = "paymentFallback")
    public String getPaymentStatusForUserRestTemplate(@PathVariable String userId) {

        System.out.println("RestTemplate way...");
        String url = BASE_URL_PAYMENT + "/payment-service/payment/" + userId;
        System.out.println("retry method called " + attempt++ + " times " + " at " + new Date());
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    //@GetMapping("/payment/{userId}")
    //@Retry(name = ORDER_SERVICE, fallbackMethod = "paymentFallback")
    //@CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "paymentFallback")
//    public String getPaymentStatusForUserRestTemplateRev(@PathVariable String userId) {
//        //try {
//        System.out.println("retry first RestTemplate way..." + userId);
//        String url = BASE_URL_PAYMENT + "/payment-service/payment/" + userId;
//        System.out.println("retry method called " + attempt++ + " times " + " at " + new Date());
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        return response.getBody();
//        /*} catch (Exception e) {
//            System.out.println("Exception while calling payment-service : " + e.getMessage());
//            return "RestTemplate catch : Payment-service TIMED OUT or failed for userId: " + userId;
//        }*/
//    }

}
