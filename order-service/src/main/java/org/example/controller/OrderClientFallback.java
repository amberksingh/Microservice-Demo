package org.example.controller;

import org.springframework.stereotype.Component;

@Component
class OrderClientFallback implements OrderClient {
    @Override
    public String getPaymentStatusForUser(String userId) {
        return "Fallback using feign component : OrderClientFallback : Payment-service TIMED OUT for userId: " + userId;
    }
}
