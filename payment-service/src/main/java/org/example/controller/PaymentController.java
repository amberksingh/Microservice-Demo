package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment-service")
public class PaymentController {

    int attempt = 1;

    @GetMapping("/dummy")
    public String dummyMessage() {
        System.out.println("Hello..dummy message from payment-service");
        return "Hello..dummy message from payment-service";
    }

    @GetMapping("/payment/{userId}")
    public String getPaymentStatusForUser(@PathVariable("userId") String userId) throws InterruptedException {
        System.out.println("Attempt in payment-service: "+attempt);
        attempt++;
        System.out.println("payment-service : trying payment for userId : " + userId);
        Thread.sleep(9000);
        return "payment-service : payment success for userId : " + userId;
    }

//    @GetMapping("/orders/{userId}")
//    List<String> getOrdersForUser(@PathVariable("userId") String userId);
}
