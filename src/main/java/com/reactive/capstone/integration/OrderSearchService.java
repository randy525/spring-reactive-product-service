package com.reactive.capstone.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSearchService {

    private final WebClient orderSearchServiceWebClient;

    public Flux<OrderDto> fetchOrdersByPhoneNumber(String phoneNumber) {
        return orderSearchServiceWebClient.get()
                .uri("/orderSearchService/order/phone?phoneNumber={phoneNumber}", phoneNumber)
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .doOnEach(productInfo -> {
                    String requestId = productInfo.getContextView().get("requestId");
                    MDC.put("requestId", requestId);
                })
                .doOnNext(orderInfo -> log.info("Fetched order info: {}; Request ID: {}", orderInfo, MDC.get("requestId")))
                .onErrorResume(e -> {
                    log.error("Error fetching orders by phone number: {}", phoneNumber, e);
                    return Flux.empty();
                });
    }

}
