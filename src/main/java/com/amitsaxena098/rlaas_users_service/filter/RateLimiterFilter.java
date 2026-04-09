package com.amitsaxena098.rlaas_users_service.filter;

import com.amitsaxena098.rlaas_users_service.interfaces.RateLimitingAlgorithm;
import com.amitsaxena098.rlaas_users_service.model.RateLimitResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {
    public static final String USER_ID = "userId";
    private final RateLimitingAlgorithm rateLimitingAlgorithm;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getParameter(USER_ID);
        RateLimitResponse rateLimitResponse = rateLimitingAlgorithm.allowRequest(userId);
        if(!rateLimitResponse.allowed()) {
            response.setStatus(429);
            response.getWriter().write("User is not allowed" + "...Try after " + rateLimitResponse.ttl() + " seconds.");
            return ;
        }
        filterChain.doFilter(request, response);
    }
}
