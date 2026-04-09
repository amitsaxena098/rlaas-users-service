package com.amitsaxena098.rlaas_users_service.interfaces;

import com.amitsaxena098.rlaas_users_service.model.RateLimitResponse;

public interface RateLimitingAlgorithm {

    RateLimitResponse allowRequest(String userId);
}
