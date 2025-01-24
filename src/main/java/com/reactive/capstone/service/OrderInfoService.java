package com.reactive.capstone.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProductsService {

    private final WebClient orderSearchServiceWebClient;

    private final WebClient productInfoServiceWebClient;

    public List<Object> getOrderInfo(String userId) {
        return null;
    }

}
