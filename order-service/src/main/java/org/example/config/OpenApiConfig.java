package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("Endpoints for order processing and Kafka/WebClient demos")
                        .version("1.0.0"));
    }
}
