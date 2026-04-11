package com.amitsaxena098.rlaas_users_service.config;

import com.amitsaxena098.rlaas_users_service.util.ScriptLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public DefaultRedisScript<List> rateLimiterScript() {
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setScriptText(ScriptLoader.loadScript("fixedWindowSize.lua"));
        script.setResultType(List.class);
        return script;
    }
}
