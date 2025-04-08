package com.example.backendservice.service;

import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserPageResponse;
import com.example.backendservice.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    UserPageResponse findAll(String keyword, String sort, int page, int size);
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    long save(UserCreationRequest req);
    void update(UserUpdateRequest req);
    void updatePassword(UserPasswordRequest req);
    void delete(Long id);
}
