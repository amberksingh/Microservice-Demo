package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//public abstract class PaymentEvent {}
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class PaymentEvent {
    private String eventType; // COMPLETED or FAILED
}