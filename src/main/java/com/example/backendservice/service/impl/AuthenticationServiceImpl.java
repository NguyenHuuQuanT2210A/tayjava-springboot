package com.example.backendservice.service.impl;

import com.example.backendservice.controller.request.SignInRequest;
import com.example.backendservice.controller.response.TokenResponse;
import com.example.backendservice.model.UserEntity;
import com.example.backendservice.repository.UserRepository;
import com.example.backendservice.service.AuthenticationService;
import com.example.backendservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.backendservice.common.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("Generating access token for user: {}", request.getUsername());

        List<String> authorities = new ArrayList<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            log.info("isAuthenticated: {}", authentication.isAuthenticated());
            log.info("Authorities: {}", authentication.getAuthorities());
            authorities.add(authentication.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.error("Login failed, message: {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        String accessToken =  jwtService.generateAccessToken(
                request.getUsername(),
                authorities
        );

        String refreshToken = jwtService.generateRefreshToken(
                request.getUsername(),
                authorities
        );
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);

        UserEntity user = userRepository.findByUsername(username);
        List<String> authorities = new ArrayList<>();
        user.getAuthorities()
                .forEach(authority -> authorities.add(authority.getAuthority()));

        String accessToken = jwtService.generateAccessToken(
                username,
                authorities
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
