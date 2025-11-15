package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.PaymentRequest;
import org.example.entity.Order;
import org.example.repo.OrderJdbcRepo;
import org.example.repo.OrderJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.example.config.Constants.BASE_URL_PAYMENT;

@RestController
@RequestMapping("/order-table")
public class OrderTableController {

    public static final String PAYMENT_TABLE = BASE_URL_PAYMENT + "/payment-table/";

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


    //swagger
    @Operation(
            summary = "Add a new order",
            description = "Creates a new order record in the database.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Order.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Sample Order",
                                            value = """
                                        {
                                          "orderNumber": "ORD-1001",
                                          "productName": "Dell Inspiron 15",
                                          "amount": 45000,
                                          "status": "CREATED"
                                        }
                                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order created successfully",
                            content = @Content(schema = @Schema(implementation = Order.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid order details"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            }
    )
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

    @PostMapping("/addOrderWithPaymentPostForEntity")
    public ResponseEntity<Order> addOrderWithPaymentPostForEntity(@RequestBody Order order) {
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
        String paymentServiceUrl = PAYMENT_TABLE + "paymentWithOrderDetailsForEntity";
        ResponseEntity<String> paymentResponse =
                restTemplate.postForEntity(paymentServiceUrl, new HttpEntity<>(paymentRequest), String.class);

        System.out.println("💳 Payment Service Response: " + paymentResponse.getBody());

        // Step 4: Optionally update order status
        savedOrder.setStatus("PAID");
        jpaRepo.save(savedOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PostMapping("/addOrderWithPaymentPostForObject")
    public ResponseEntity<String> addOrderWithPaymentPostForObject(@RequestBody Order order) {
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
        String paymentServiceUrl = PAYMENT_TABLE + "paymentWithOrderDetailsForObject";

        String response = restTemplate.postForObject(paymentServiceUrl, paymentRequest, String.class);

        System.out.println("💳 Payment Service Response: " + response);

        // Step 4: Optionally update order status
        savedOrder.setStatus("PAID");
        jpaRepo.save(savedOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //fix
    @GetMapping("/fetchOrderByPaymentMethodForObject/{paymentMethod}")
    List<PaymentRequest> fetchOrderByPaymentMethodForObject(@PathVariable("paymentMethod") String paymentMethod) {

        String paymentServiceUrl = PAYMENT_TABLE + "fetchOrderByPaymentMethodForObject/" + paymentMethod;
        // RestTemplate doesn't automatically convert generic List<T>, so we use an array type
        PaymentRequest[] payments = restTemplate.getForObject(paymentServiceUrl, PaymentRequest[].class, paymentMethod);

        if (payments == null || payments.length == 0) {
            return List.of();
        }
        System.out.println("all payments for paymentMethod " + paymentMethod + " : ");
        List<PaymentRequest> list = Arrays.asList(payments);
        list.forEach(System.out::println);
        return list;
    }

    @GetMapping("/fetchOrderByPaymentMethodForEntity/{paymentMethod}")
    ResponseEntity<List<PaymentRequest>> fetchOrderByPaymentMethodForEntity(@PathVariable("paymentMethod") String paymentMethod) {

        String paymentServiceUrl = PAYMENT_TABLE + "fetchOrderByPaymentMethodForEntity/" + paymentMethod;
        // RestTemplate doesn't automatically convert generic List<T>, so we use an array type
        ResponseEntity<PaymentRequest[]> response = restTemplate.getForEntity(paymentServiceUrl, PaymentRequest[].class, paymentMethod);

        // ✅ You can now access:
        HttpStatusCode status = response.getStatusCode();
        HttpHeaders headers = response.getHeaders();
        PaymentRequest[] payments = response.getBody();

        System.out.println("HTTP Status: " + status);
        System.out.println("Response Headers: " + headers);
        List<PaymentRequest> list = Arrays.stream(payments).toList();
        System.out.println("all payments for paymentMethod " + paymentMethod + " : ");
        list.forEach(System.out::println);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
