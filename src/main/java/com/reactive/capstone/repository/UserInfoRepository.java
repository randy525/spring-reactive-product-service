package com.reactive.capstone.repository;


import com.reactive.capstone.domain.UserInfoEntity;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserInfoRepository extends ReactiveMongoRepository<UserInfoEntity, String> {

    Mono<UserInfoEntity> findById(String id);

}
