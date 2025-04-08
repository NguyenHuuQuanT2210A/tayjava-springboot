package com.example.backendservice.controller.request;

import lombok.Getter;

@Getter
public class UserPasswordRequest {
    private Long id;
    private String password;
    private String confirmPassword;
}
