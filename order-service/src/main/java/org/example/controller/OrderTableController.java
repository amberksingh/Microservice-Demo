package org.example.controller;

import org.example.entity.Order;
import org.example.repo.OrderJdbcRepo;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order-table")
public class OrderTableController {

    @Autowired
    OrderJpaRepo jpaRepo;

    @Autowired
    OrderJdbcRepo jdbcRepo;

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
}
