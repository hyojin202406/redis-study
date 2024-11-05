package com.example.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Redis의 ZADD, ZRANGE, ZREM 명령을 RedisTemplate의 opsForZSet()을 통해 사용할 수 있습니다.
 */
@Service
@RequiredArgsConstructor
public class PriorityQueueService {

    private static final String QUEUE_KEY = "userPriorityQueue"; // Sorted Set 키

    private final RedisTemplate<String, String> redisTemplate;

    // 대기열에 사용자 추가 (score는 우선순위, 낮을수록 높은 우선순위)
    public void enqueueUser(String userId, double priorityScore) {
        redisTemplate.opsForZSet().add(QUEUE_KEY, userId, priorityScore);
    }

    // 대기열에서 가장 높은 우선순위의 사용자 가져오기
    public String dequeueUser() {
        // 우선순위가 가장 높은 사용자 가져오기
        Set<String> users = redisTemplate.opsForZSet().range(QUEUE_KEY, 0, 0);
        if (users == null || users.isEmpty()) {
            return null;
        }
        String userId = users.iterator().next();

        // 가져온 사용자 대기열에서 제거
        redisTemplate.opsForZSet().remove(QUEUE_KEY, userId);
        return userId;
    }

    // 대기열 크기 확인
    public Long getQueueSize() {
        return redisTemplate.opsForZSet().zCard(QUEUE_KEY);
    }

    // 모든 대기열 사용자 보기 (디버그용)
    public Set<String> getAllUsers() {
        return redisTemplate.opsForZSet().range(QUEUE_KEY, 0, -1);
    }
}