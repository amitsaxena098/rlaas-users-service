package com.amitsaxena098.rlaas_users_service.model;


public record RateLimitResponse(boolean allowed, long currentCount, long ttl) {
}