package com.example.backendservice.service;

import com.example.backendservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public record UserServiceDetail(UserRepository userRepository) {

    public UserDetailsService UserDetailsService() {
        return userRepository::findByUsername;
    }
}
