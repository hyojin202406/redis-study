package com.example.redis;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class RedisTestContainerConfig {
    static final String REDIS_IMAGE = "redis:6-alpine";
    static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE) // (1)
                .withExposedPorts(6379) // (2)
                .withReuse(true); // (3)
        REDIS_CONTAINER.start(); // (4)
    }

    @DynamicPropertySource // (5)
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.redis.password", () -> "password");
    }
}