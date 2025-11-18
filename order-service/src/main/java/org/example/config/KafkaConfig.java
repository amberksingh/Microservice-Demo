package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.config.Constants.*;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(ORDER_EVENTS_TOPIC)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name(PAYMENT_EVENTS_TOPIC)
                .partitions(4)
                .replicas(1)
                .build();
    }



//    @Bean
//    public NewTopic paymentSuccessTopic() {
//        return TopicBuilder.name("payment-success-events")
//                .partitions(4)
//                .replicas(1)
//                .build();
//    }
//
//    @Bean
//    public NewTopic paymentFailedTopic() {
//        return TopicBuilder.name("payment-fail-events")
//                .partitions(4)
//                .replicas(1)
//                .build();
//    }
}
