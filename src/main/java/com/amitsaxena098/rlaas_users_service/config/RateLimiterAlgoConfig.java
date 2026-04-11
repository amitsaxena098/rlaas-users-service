package com.amitsaxena098.rlaas_users_service.config;

import com.amitsaxena098.rlaas_users_service.interfaces.RateLimitingAlgorithm;
import com.amitsaxena098.rlaas_users_service.service.FixedWindowSizeRateLimiting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

@Configuration
public class RateLimiterAlgoConfig {

    @Value("${rate-limiter.window-size}")
    private int WINDOW_SIZE ;
    @Value("${rate-limiter.max-requests}")
    private int MAX_REQUESTS;

    @Bean
    @ConditionalOnProperty(name = "rate-limiter.use-algo", havingValue = "fixed-window-algo")
    public RateLimitingAlgorithm fixedWindowAlgo(StringRedisTemplate redisTemplate,
                                                 RedisScript<List> rateLimitScript) {
        return new FixedWindowSizeRateLimiting(redisTemplate, rateLimitScript, WINDOW_SIZE, MAX_REQUESTS);
    }
}
