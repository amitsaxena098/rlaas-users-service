package com.amitsaxena098.rlaas_users_service.config;

import com.amitsaxena098.rlaas_users_service.util.ScriptLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean("fixedWindowScript")
    @ConditionalOnProperty(name = "rate-limiter.use-algo", havingValue = "fixed-window-algo")
    public DefaultRedisScript<List> rateLimiterScript() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setScriptText(ScriptLoader.loadScript("scripts/fixedWindowSize.lua"));
        script.setResultType(List.class);
        return script;
    }
}
