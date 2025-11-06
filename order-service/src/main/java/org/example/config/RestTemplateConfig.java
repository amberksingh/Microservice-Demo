package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${app.restTemplate.connectTimeout:2000}")
    private int connectTimeout;

    @Value("${app.restTemplate.readTimeout:3000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //System.out.println("connectTimeout = " + connectTimeout);
        //System.out.println("readTimeout = " + readTimeout);
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))  // connection timeout
                .setReadTimeout(Duration.ofMillis(readTimeout))     // read timeout
                .build();
    }
}
