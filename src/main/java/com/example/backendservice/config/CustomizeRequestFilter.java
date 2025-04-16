package com.example.backendservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j(topic = "CUSTOMER_REQUEST_FILTER")
public class CustomizeRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Custom logic before the request is processed
        log.info("{} {}", request.getMethod(), request.getRequestURI());

        // Continue with the filter chain
        filterChain.doFilter(request, response);

        // Custom logic after the request is processed
        System.out.println("Custom filter logic after processing the request");
    }
}
