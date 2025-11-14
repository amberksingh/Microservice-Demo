package org.example.events;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent implements Serializable {

    private Long orderId;
    private Long userId;           // for future User service
    private Double amount;
    private String productName;
    private String orderStatus;    // CREATED
}
