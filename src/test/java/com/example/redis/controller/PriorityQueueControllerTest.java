package com.example.redis.controller;

import com.example.redis.controller.dto.UserPriorityDTO;
import com.example.redis.service.PriorityQueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Testcontainers
class PriorityQueueControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PriorityQueueService queueService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Redis Testcontainers 설정
    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);

    @BeforeAll
    static void setUpRedis() {
        // Redis 서버의 포트와 호스트 정보를 설정
        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testUserLogin() throws Exception {
        int userCount = 20; // 호출할 유저 수
        for (int i = 1; i <= userCount; i++) {
            // Arrange
            UserPriorityDTO userPriorityDTO = new UserPriorityDTO("user" + i);

            // Act & Assert
            mockMvc.perform(post("/priority-queue/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userPriorityDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("user" + i + "님이 대기열에 추가되었습니다. 대기 순번: " + i));
        }
    }

    @Test
    void testGetQueueSize() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/priority-queue/size"))
                .andExpect(status().isOk())
                .andExpect(content().string("1")); // 테스트 실행 전에 1명이 추가된 상태로 가정
    }

    @Test
    void testGetUserStatus() throws Exception {
        // Arrange
        String userId = "user21";
        queueService.userLogin(userId, 21.0); // 우선순위 점수를 지정하여 사용자 추가

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/priority-queue/status/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn();

        // Log the content
        String responseContent = result.getResponse().getContentAsString();
        log.info("Response content: {}", responseContent);
    }
}
