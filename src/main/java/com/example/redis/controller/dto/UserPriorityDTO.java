package com.example.redis.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPriorityDTO {
    private String userId;
    private String token;

    // userId를 받아 token을 자동 생성하는 생성자
    public UserPriorityDTO(String userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString(); // token 생성
    }
}