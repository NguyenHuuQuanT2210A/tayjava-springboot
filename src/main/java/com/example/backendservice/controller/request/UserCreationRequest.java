package com.example.backendservice.controller.request;

import com.example.backendservice.common.Gender;
import com.example.backendservice.common.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class UserCreationRequest {
    private String firstName;
    private String lastName;
    private String username;
    private Gender gender;
    private String birthday;
    private String email;
    private String phone;
    private UserType type;
    private List<AddressRequest> addresses; //home, office
}
