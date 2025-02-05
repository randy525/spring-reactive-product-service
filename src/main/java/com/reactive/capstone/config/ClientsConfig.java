package com.reactive.capstone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsConfig {

    @Value("${integration.order-search-service.base-url}")
    private String orderSearchServiceUrl;

    @Value("${integration.product-info-service.base-url}")
    private String productInfoServiceUrl;

    @Bean
    public WebClient orderSearchServiceWebClient() {
        return WebClient.builder()
                .baseUrl(orderSearchServiceUrl)
                .build();
    }

    @Bean
    public WebClient productInfoServiceWebClient() {
        return WebClient.builder()
                .baseUrl(productInfoServiceUrl)
                .build();
    }

}
