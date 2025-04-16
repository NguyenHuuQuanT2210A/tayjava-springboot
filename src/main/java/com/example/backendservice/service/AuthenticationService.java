package com.example.backendservice.service;

import com.example.backendservice.controller.request.SignInRequest;
import com.example.backendservice.controller.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String refreshToken);
}
