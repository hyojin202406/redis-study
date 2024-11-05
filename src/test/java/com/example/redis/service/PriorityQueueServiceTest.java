package com.example.redis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PriorityQueueServiceTest {
    @Autowired
    private PriorityQueueService priorityQueueService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String QUEUE_KEY = "userPriorityQueue";
    private static final String ACTIVE_QUEUE_KEY = "activeUserQueue";

//    @BeforeEach
//    void setUp() {
//        // Redis 테스트 데이터 초기화
//        redisTemplate.delete(QUEUE_KEY);
//        redisTemplate.delete(ACTIVE_QUEUE_KEY);
//    }

    @Test
    void testIsUserActive() {
        // Given
        String userId = "user7";

        // When & Then
        // 서버가 실행되면 스케줄러를 호출하여 활성 대기열로 사용자 추가합니다.
        assertTrue(priorityQueueService.isUserActive(userId));
    }
}