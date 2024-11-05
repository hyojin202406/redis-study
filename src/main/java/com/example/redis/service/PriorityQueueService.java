package com.example.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis의 ZADD, ZRANGE, ZREM 명령을 RedisTemplate의 opsForZSet()을 통해 사용할 수 있습니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PriorityQueueService {
    private static final String QUEUE_KEY = "userPriorityQueue"; // Sorted Set 키
    private static final String ACTIVE_QUEUE_KEY = "activeUserQueue"; // 활성 사용자 대기열 키

    private final RedisTemplate<String, String> redisTemplate;

    // 사용자 접속 시 호출되는 메서드
    public void userLogin(String userId, double priorityScore) {
        redisTemplate.opsForZSet().add(QUEUE_KEY, userId, priorityScore);
    }

    // 스케줄러로 활성 유저 대기열에 3명씩 추가
    @Scheduled(fixedRate = 180000) // 3분마다 실행
    public void activateUsers() {
        log.info("스케줄러 시작");
        Set<String> users = redisTemplate.opsForZSet().range(QUEUE_KEY, 0, 2); // Sorted Set에서 3명 가져오기
        log.info("users : {}", users);
        if (users != null && !users.isEmpty()) {
            for (String user : users) {
                // 활성 사용자 대기열에 추가
                redisTemplate.opsForList().rightPush(ACTIVE_QUEUE_KEY, user);
                // 대기열에서 제거
                redisTemplate.opsForZSet().remove(QUEUE_KEY, user);
            }
            // TTL 설정: 1분 (60초), 한번만 설정하기 위해 조건 추가
            if (redisTemplate.getExpire(ACTIVE_QUEUE_KEY) == -1) {
                redisTemplate.expire(ACTIVE_QUEUE_KEY, 60, TimeUnit.SECONDS);
            }
        }
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