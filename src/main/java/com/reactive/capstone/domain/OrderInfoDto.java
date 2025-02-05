package com.reactive.capstone.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfoDto {

    private String orderNumber;
    private String userName;
    private String phoneNumber;
    private String productCode;
    private String productName;
    private String productId;

}
