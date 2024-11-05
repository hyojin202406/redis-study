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
    public String userLogin(@RequestBody UserPriorityDTO userPriorityDTO) {
        double priorityScore = calculatePriorityScore();
        queueService.userLogin(userPriorityDTO.getUserId(), priorityScore);
        return userPriorityDTO.getUserId() + "님이 대기열에 추가되었습니다. 대기 순번: " + (int) priorityScore;
    }

    // 대기열 크기 확인
    @GetMapping("/size")
    public Long getQueueSize() {
        return queueService.getQueueSize();
    }

    // 대기 상태 조회
    @GetMapping("/status/{userId}")
    public String getUserStatus(@PathVariable("userId") String userId) {
        int position = queueService.getUserPosition(userId);
        return "대기 상태: " + (position == -1 ? "대기열에 없음" : "대기 순번: " + position);
    }

    // 우선순위 점수 계산 로직
    private double calculatePriorityScore() {
        double currentScore = userPriority.get();
        double newScore = currentScore + 1.0;
        userPriority.set(newScore);
        return newScore;
    }
}