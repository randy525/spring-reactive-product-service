package com.reactive.capstone.controller;

import com.reactive.capstone.domain.OrderInfoDto;
import com.reactive.capstone.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    @GetMapping("/user-orders")
    public Flux<OrderInfoDto> getUserOrdersInfo(@RequestParam String userId, @RequestHeader String requestId) {
        return orderInfoService.getOrderInfo(userId)
                .contextWrite(Context.of("requestId", requestId));
    }

}
