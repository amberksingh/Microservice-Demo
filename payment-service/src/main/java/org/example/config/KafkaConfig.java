package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.config.Constants.USER_BALANCE_REQUESTS;
import static org.example.config.Constants.USER_BALANCE_RESPONSES;

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

}
