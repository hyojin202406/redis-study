package com.example.redis.controller.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPriorityDTO {
    private String userId;
    private double priorityScore;
}