package com.example.backendservice.service;

import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    long save(UserCreationRequest req);
    void update(UserUpdateRequest req);
    void updatePassword(UserPasswordRequest req);
    void delete(Long id);
}
