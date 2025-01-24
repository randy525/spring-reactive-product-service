package com.reactive.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
public class UserInfoEntity {

    @Id
    private String id;
    private String name;
    private String phone;
}
