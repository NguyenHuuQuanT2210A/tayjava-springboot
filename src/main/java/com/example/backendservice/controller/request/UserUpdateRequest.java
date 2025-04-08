package com.example.backendservice.controller.request;

import com.example.backendservice.common.Gender;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserUpdateRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Gender gender;
    private String birthday;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
