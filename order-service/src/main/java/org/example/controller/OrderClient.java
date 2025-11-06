package org.example.controller;

import org.example.config.Constants;
import org.example.controller.OrderClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.example.config.Constants.ORDER_SERVICE;

@FeignClient(
        name = ORDER_SERVICE,
        url = Constants.BASE_URL_PAYMENT//,
        //fallback = OrderClientFallback.class
)
public interface OrderClient {

    @GetMapping("/payment-service/payment/{userId}")
    String getPaymentStatusForUser(@PathVariable("userId") String userId);
}

