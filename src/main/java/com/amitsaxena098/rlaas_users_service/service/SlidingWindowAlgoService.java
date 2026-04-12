package com.amitsaxena098.rlaas_users_service.service;

import com.amitsaxena098.rlaas_users_service.interfaces.RateLimitingAlgorithm;
import com.amitsaxena098.rlaas_users_service.model.RateLimitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class SlidingWindowAlgoService implements RateLimitingAlgorithm {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<List> script;
    private final int windowSize;
    private final int maxRequests;
    @Override
    public RateLimitResponse allowRequest(String userId) {
        String keyPrefix = "rlaas:rate_limit:" + userId;

        List result =  stringRedisTemplate.execute(
                script, Collections.singletonList(keyPrefix),
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(windowSize),
                String.valueOf(maxRequests)
        ).stream().toList();
        if (result.size() < 3) {
            throw new RuntimeException("Invalid Redis response");
        }

        boolean allowed = (Long) result.get(0) == 1;
        long currentCount = (Long)result.get(1);
        long ttl = (Long) result.get(2);

        return new RateLimitResponse(allowed, currentCount, ttl);
    }
}
