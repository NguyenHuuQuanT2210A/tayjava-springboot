package com.example.backendservice.controller.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String gender;
    private String birthday;
    private String email;
    private String phone;
}
