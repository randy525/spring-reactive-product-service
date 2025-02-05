package com.reactive.capstone.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductInfoService {

    private final WebClient productInfoServiceWebClient;

    public Flux<ProductInfoDto> fetchProductInfoByProductCode(String productCode) {
        return productInfoServiceWebClient.get()
                .uri("/productInfoService/product/names?productCode={productCode}", productCode)
                .retrieve()
                .bodyToFlux(ProductInfoDto.class)
                .doOnEach(productInfo -> {
                    String requestId = productInfo.getContextView().get("requestId");
                    MDC.put("requestId", requestId);
                })
                .doOnNext(productInfo -> log.info("Fetched product info: {}", productInfo))
                .onErrorResume(e -> {
                    log.error("Error fetching products with productCode: {}", productCode, e);
                    return Flux.empty();
                });

    }

}
