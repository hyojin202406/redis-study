package com.example.redis.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.redisson.Redisson;
import org.redisson.config.Config;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // Redisson Client 설정
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }

    // Redisson ConnectionFactory 설정
    @Bean
    public RedissonConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    // RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedissonConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return redisTemplate;
    }
}
