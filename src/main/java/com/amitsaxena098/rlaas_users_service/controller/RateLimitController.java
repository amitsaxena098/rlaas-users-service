package com.amitsaxena098.rlaas_users_service.controller;

import com.amitsaxena098.rlaas_users_service.model.RateLimitResponse;
import com.amitsaxena098.rlaas_users_service.service.FixedWindowSizeRateLimiting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RateLimitController {

    private final FixedWindowSizeRateLimiting fixedWindowSizeRateLimiting;


    @GetMapping("/check/{userId}")
    public ResponseEntity<RateLimitResponse> checkRateLimit(@PathVariable String userId) {
        RateLimitResponse rateLimitResponse = fixedWindowSizeRateLimiting.allowRequest(userId);
        if(!rateLimitResponse.allowed()) {
            return new ResponseEntity<>(new RateLimitResponse(false,
                    rateLimitResponse.currentCount(), rateLimitResponse.ttl(),
            "User not allowed. Too many requests...."), HttpStatus.TOO_MANY_REQUESTS);
        }
        return new ResponseEntity<>(rateLimitResponse, HttpStatus.OK);

    }
}
