package com.reactive.capstone.service;

import com.reactive.capstone.domain.UserInfoDto;
import com.reactive.capstone.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public Mono<UserInfoDto> findUserInfoById(String userId) {
        return userInfoRepository.findById(userId)
                .map(user -> UserInfoDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .phone(user.getPhone())
                        .build())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

}
