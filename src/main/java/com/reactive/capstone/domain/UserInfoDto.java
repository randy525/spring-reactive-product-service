package com.reactive.capstone.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

    private String id;
    private String name;
    private String phone;

}
