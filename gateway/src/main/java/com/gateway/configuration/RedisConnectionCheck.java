package com.gateway.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

public class RedisConnectionCheck {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void checkRedis() {
        redisTemplate.opsForValue().set("test-key", "test-value").subscribe();
        redisTemplate.opsForValue().get("test-key")
                .subscribe(value -> System.out.println("Redis test value: " + value));
    }
}
