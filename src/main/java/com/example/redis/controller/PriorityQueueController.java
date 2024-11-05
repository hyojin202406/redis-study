package com.example.redis.controller;

import com.example.redis.controller.dto.UserPriorityDTO;
import com.example.redis.service.PriorityQueueService;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priority-queue")
@RequiredArgsConstructor
public class PriorityQueueController {

    private final PriorityQueueService queueService;
    private final AtomicDouble userPriority = new AtomicDouble(0.0);

    // 사용자 대기열에 추가 (우선순위 점수와 함께)
    @PostMapping("/login")
    public String userLoginr(@RequestBody UserPriorityDTO userPriority) {
        double priorityScore = calculatePriorityScore();
        queueService.userLogin(userPriority.getUserId(), priorityScore);
        return userPriority.getUserId() + "님이 대기열에 추가되었습니다.";
    }

    // 대기열 크기 확인
    @GetMapping("/size")
    public Long getQueueSize() {
        return queueService.getQueueSize();
    }

    // 우선순위 점수 계산 로직
    private double calculatePriorityScore() {
        double currentScore = userPriority.get();
        double newScore = currentScore + 1.0;
        userPriority.set(newScore);
        return newScore;
    }
}