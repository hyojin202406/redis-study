package com.example.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private static final String QUEUE_KEY = "userQueue"; // 대기열 키

    private final RedisTemplate<String, String> redisTemplate;

    // 사용자 대기열에 추가
    public void enqueueUser(String userId) {
        redisTemplate.opsForList().leftPush(QUEUE_KEY, userId);
    }

    // 대기열에서 다음 사용자 처리
    public String dequeueUser() {
        return redisTemplate.opsForList().rightPop(QUEUE_KEY);
    }

    // 대기열에 대기 중인 사용자 수 확인
    public Long getQueueSize() {
        return redisTemplate.opsForList().size(QUEUE_KEY);
    }
}
