package com.reactive.capstone.service;

import com.reactive.capstone.domain.OrderInfoDto;
import com.reactive.capstone.domain.UserInfoDto;
import com.reactive.capstone.integration.OrderDto;
import com.reactive.capstone.integration.OrderSearchService;
import com.reactive.capstone.integration.ProductInfoDto;
import com.reactive.capstone.integration.ProductInfoService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderSearchService orderSearchService;
    private final ProductInfoService productInfoService;
    private final UserInfoService userInfoService;

    public Flux<OrderInfoDto> getOrderInfo(String userId) {
        return userInfoService.findUserInfoById(userId)
                .flatMapMany(user -> orderSearchService.fetchOrdersByPhoneNumber(user.getPhone())
                        .flatMap(order -> productInfoService.fetchProductInfoByProductCode(order.getProductCode())
                                .reduce(productWithMaxScoreFunction())
                                .map(product -> buildOrderInfoDto(user, order, product))));
    }

    private OrderInfoDto buildOrderInfoDto(UserInfoDto user, OrderDto order, ProductInfoDto product) {
        return OrderInfoDto.builder()
                .orderNumber(order.getOrderNumber())
                .userName(user.getName())
                .phoneNumber(order.getPhoneNumber())
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .productId(product.getProductId())
                .build();
    }

    private BiFunction<ProductInfoDto, ProductInfoDto, ProductInfoDto> productWithMaxScoreFunction() {
        return (product1, product2) -> product1.getScore() > product2.getScore() ? product1 : product2;
    }

}
