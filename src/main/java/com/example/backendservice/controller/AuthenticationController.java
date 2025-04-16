package com.example.backendservice.controller;

import com.example.backendservice.controller.request.SignInRequest;
import com.example.backendservice.controller.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthenticationController {

    @Operation(summary = "Access token", description = "Get access token and refresh token by username and password")
    @PostMapping(value = "/access-token")
    public TokenResponse getAccessToken(@RequestBody SignInRequest request) {
        log.info("Received login request for username: {}", request.getUsername());
        // Implement your authentication logic here
        return TokenResponse.builder()
                .accessToken("DUMMY-ACCESS-TOKEN")
                .refreshToken("DUMMY-REFRESH-TOKEN")
                .build();
    }

    @Operation(summary = "Refresh token", description = "Get new access token by refresh token")
    @PostMapping(value = "/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody String refreshToken) {
        log.info("Received refresh token request for token: {}", refreshToken);
        return TokenResponse.builder()
                .accessToken("DUMMY-NEW-ACCESS-TOKEN")
                .refreshToken("DUMMY-REFRESH-TOKEN")
                .build();
    }
}
