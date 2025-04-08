package com.example.backendservice.controller.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String gender;
    private String birthday;
    private String email;
    private String phone;
    // more
}
