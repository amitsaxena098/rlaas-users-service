package com.amitsaxena098.rlaas_users_service.service;

import com.amitsaxena098.rlaas_users_service.model.RateLimitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedWindowSizeRateLimiting {
    private static final int WINDOW_SIZE = 60;
    private static final int MAX_REQUESTS = 5;

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<List> script;

    public RateLimitResponse allowRequest(String userId) {
        String keyPrefix = "rlaas:rate_limit:" + userId;

        List result =  stringRedisTemplate.execute(
                script, Collections.singletonList(keyPrefix),
                String.valueOf(WINDOW_SIZE),
                String.valueOf(MAX_REQUESTS)
        ).stream().toList();
        if (result.size() < 3) {
            throw new RuntimeException("Invalid Redis response");
        }

        boolean allowed = (Long) result.get(0) == 1;
        long currentCount = (Long)result.get(1);
        long ttl = (Long) result.get(2);

        return new RateLimitResponse(allowed, currentCount, ttl, "User is allowed");
    }


}
