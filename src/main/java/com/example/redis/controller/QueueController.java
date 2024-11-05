package com.example.redis.controller;

import com.example.redis.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    @Autowired
    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    // 사용자 대기열에 추가
    @PostMapping("/enqueue")
    public String enqueueUser(@RequestParam String userId) {
        queueService.enqueueUser(userId);
        return userId + "님이 대기열에 추가되었습니다.";
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
}
