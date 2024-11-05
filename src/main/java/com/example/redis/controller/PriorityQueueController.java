package com.example.redis.controller;

import com.example.redis.controller.dto.UserPriorityDTO;
import com.example.redis.service.PriorityQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/priority-queue")
public class PriorityQueueController {

    private final PriorityQueueService queueService;

    @Autowired
    public PriorityQueueController(PriorityQueueService queueService) {
        this.queueService = queueService;
    }

    // 사용자 대기열에 추가 (우선순위 점수와 함께)
    @PostMapping("/enqueue")
    public String enqueueUser(@RequestBody UserPriorityDTO userPriority) {
        queueService.enqueueUser(userPriority.getUserId(), userPriority.getPriorityScore());
        return userPriority.getUserId() + "님이 우선순위 " + userPriority.getPriorityScore() + "로 대기열에 추가되었습니다.";
    }

    // 대기열에서 사용자 처리
    @PostMapping("/dequeue")
    public String dequeueUser() {
        String userId = queueService.dequeueUser();
        return userId != null ? userId + "님이 처리되었습니다." : "대기열이 비어 있습니다.";
    }

    // 대기열 크기 확인
    @GetMapping("/size")
    public Long getQueueSize() {
        return queueService.getQueueSize();
    }

    // 모든 대기열 사용자 확인 (디버그용)
    @GetMapping("/all-users")
    public Set<String> getAllUsers() {
        return queueService.getAllUsers();
    }
}