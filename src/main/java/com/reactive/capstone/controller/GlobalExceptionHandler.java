package com.reactive.capstone.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<String>> handleRuntimeException(RuntimeException e, ServerWebExchange exchange) {
        putRequestIdInMDC(exchange);
        log.error(e.getMessage(), e);
        return Mono.just(ResponseEntity.status(NOT_FOUND).body(e.getMessage()));
    }


    private void putRequestIdInMDC(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String requestId = headers.getFirst("requestId");
        MDC.put("requestId", requestId);
    }

}
