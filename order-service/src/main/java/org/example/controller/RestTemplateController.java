//package org.example.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import static org.example.config.Constants.BASE_URL_PAYMENT;
//
//@RestController
//@RequestMapping("order-service/restTemplate")
//public class RestTemplateController {
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @GetMapping("/getForObject/{id}")
//    public Long getForObjectDemo(@PathVariable Long id) {
//
//        String url = BASE_URL_PAYMENT + "/payment-service/getForObject/" + id;
//        Long forObject = restTemplate.getForObject(url, Long.class);
//        System.out.println("id from payment-service : " + forObject);
//        return forObject;
//    }
//
//}
