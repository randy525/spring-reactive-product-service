package com.reactive.capstone.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.reactive.capstone.domain.OrderInfoDto;
import com.reactive.capstone.service.OrderInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderInfoTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OrderInfoService orderInfoService;

    private WireMockServer wireMockServer;

    private final WebTestClient webTestClient = WebTestClient.bindToController(new OrderInfoController(orderInfoService)).build();

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8090);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testSuccessResponse() {
        configureStubForSuccessResponse();

        Flux<OrderInfoDto> response = webTestClient.get()
                .uri("/user-orders?userId={userId}", "user1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderInfoDto.class)
                .;

        StepVerifier.create(response)
                .assertNext(orderInfoDto -> {
                    assertEquals()
                })
                .verifyComplete();
    }

    private void configureStubForSuccessResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/orderSearchService/order/phone"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("{\n" +
                                                                             "    \"phoneNumber\": \"123456789\",\n" +
                                                                             "    \"orderNumber\": \"Order_0\",\n" +
                                                                             "    \"productCode\": \"3852\"\n" +
                                                                             "}\n" +
                                                                             "{\n" +
                                                                             "    \"phoneNumber\": \"123456789\",\n" +
                                                                             "    \"orderNumber\": \"Order_1\",\n" +
                                                                             "    \"productCode\": \"5256\"\n" +
                                                                             "}")
                                                           .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/productInfoService/product/names?productCode=3852"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("[\n" +
                                                                             "    {\n" +
                                                                             "        \"productId\": \"111\",\n" +
                                                                             "        \"productCode\": \"3852\",\n" +
                                                                             "        \"productName\": \"IceCream\",\n" +
                                                                             "        \"score\": 9256.34\n" +
                                                                             "    },\n" +
                                                                             "    {\n" +
                                                                             "        \"productId\": \"222\",\n" +
                                                                             "        \"productCode\": \"3852\",\n" +
                                                                             "        \"productName\": \"Milk\",\n" +
                                                                             "        \"score\": 1973.74\n" +
                                                                             "    }]")
                                                           .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/productInfoService/product/names?productCode=5256"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("[\n" +
                                                                             "    {\n" +
                                                                             "        \"productId\": \"111\",\n" +
                                                                             "        \"productCode\": \"5256\",\n" +
                                                                             "        \"productName\": \"IceCream\",\n" +
                                                                             "        \"score\": 1214.51\n" +
                                                                             "    },\n" +
                                                                             "    {\n" +
                                                                             "        \"productId\": \"222\",\n" +
                                                                             "        \"productCode\": \"5256\",\n" +
                                                                             "        \"productName\": \"Milk\",\n" +
                                                                             "        \"score\": 7834.4\n" +
                                                                             "    }]")
                                                           .withStatus(200)));
    }

}
