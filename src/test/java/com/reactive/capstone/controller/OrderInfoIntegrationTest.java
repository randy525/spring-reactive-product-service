package com.reactive.capstone.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.reactive.capstone.domain.OrderInfoDto;
import com.reactive.capstone.domain.UserInfoEntity;
import com.reactive.capstone.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureWebTestClient
public class OrderInfoIntegrationTest {

    @MockitoBean
    private UserInfoRepository userInfoRepository;

    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testSuccessResponse() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setPhone("123456789");
        configureStubForSuccessResponse();
        when(userInfoRepository.findById(anyString())).thenReturn(Mono.just(userInfoEntity));

        webTestClient.get()
                .uri("/user-orders?userId={userId}", "user1")
                .header("requestId", "123")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderInfoDto.class)
                .hasSize(2);
    }

    @Test
    void testWhenUserNotFound() {
        when(userInfoRepository.findById(anyString())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/user-orders?userId={userId}", "user3")
                .header("requestId", "123")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("User not found");
    }

    @Test
    void testWhenProductServiceReturnError() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setPhone("123456789");
        wireMockServer.stubFor(get(urlEqualTo("/productInfoService/product/names?productCode=*"))
                                       .willReturn(aResponse().withStatus(500)));
        when(userInfoRepository.findById(anyString())).thenReturn(Mono.just(userInfoEntity));

        webTestClient.get()
                .uri("/user-orders?userId={userId}", "user1")
                .header("requestId", "321")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderInfoDto.class)
                .hasSize(0);
    }

    private void configureStubForSuccessResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/orderSearchService/order/phone?phoneNumber=123456789"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("""
                                                                             {
                                                                                 "phoneNumber": "123456789",
                                                                                 "orderNumber": "Order_0",
                                                                                 "productCode": "3852"
                                                                             }
                                                                             {
                                                                                 "phoneNumber": "123456789",
                                                                                 "orderNumber": "Order_1",
                                                                                 "productCode": "5256"
                                                                             }
                                                                     """)
                                                           .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/productInfoService/product/names?productCode=3852"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("""
                                                                             [
                                                                                 {
                                                                                     "productId": "111",
                                                                                     "productCode": "3852",
                                                                                     "productName": "IceCream",
                                                                                     "score": 9256.34
                                                                                 },
                                                                                 {
                                                                                     "productId": "222",
                                                                                     "productCode": "3852",
                                                                                     "productName": "Milk",
                                                                                     "score": 1973.74
                                                                                 }
                                                                             ]""")
                                                           .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/productInfoService/product/names?productCode=5256"))
                                       .willReturn(aResponse()
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBody("""
                                                                             [
                                                                                 {
                                                                                     "productId": "111",
                                                                                     "productCode": "5256",
                                                                                     "productName": "IceCream",
                                                                                     "score": 1214.51
                                                                                 },
                                                                                 {
                                                                                     "productId": "222",
                                                                                     "productCode": "5256",
                                                                                     "productName": "Milk",
                                                                                     "score": 7834.4
                                                                                 }
                                                                             ]""")
                                                           .withStatus(200)));
    }

}
