package com.amitsaxena098.rlaas_users_service.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ScriptLoader {

    public static String loadScript(String resourcePath) {
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new IllegalArgumentException(
                    "Redis Lua script not found at classpath: " + resourcePath
            );
        }
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load Redis Lua script from classpath: " + resourcePath, e
            );
        }
    }
}
