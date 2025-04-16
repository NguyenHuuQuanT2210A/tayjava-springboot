package com.example.backendservice.service;

import com.example.backendservice.common.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface JwtService {
    String extractUsername(String token, TokenType tokenType);

    String generateAccessToken(String username, List<String> authorities);

    String generateRefreshToken(String username, List<String> authorities);
}
