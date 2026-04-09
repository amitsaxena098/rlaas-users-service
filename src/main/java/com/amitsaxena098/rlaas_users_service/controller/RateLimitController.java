package com.amitsaxena098.rlaas_users_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RateLimitController {

    @GetMapping("/check")
    public ResponseEntity<String> checkRateLimit(@RequestParam String userId) {
        return new ResponseEntity<>("User is allowed", HttpStatus.OK);

    }
}
