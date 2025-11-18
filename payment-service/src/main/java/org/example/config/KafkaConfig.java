package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.config.Constants.*;
import static org.example.config.Constants.ORDER_EVENTS_DLQ_TOPIC;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic balanceRequestTopic() {
        return TopicBuilder.name(USER_BALANCE_REQUESTS)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic balanceResponseTopic() {
        return TopicBuilder.name(USER_BALANCE_RESPONSES)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderRetryTopic() {
        return TopicBuilder.name(ORDER_EVENTS_RETRY_TOPIC)
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderDlqTopic() {
        return TopicBuilder.name(ORDER_EVENTS_DLQ_TOPIC)
                .partitions(4)
                .replicas(1)
                .build();
    }

}
