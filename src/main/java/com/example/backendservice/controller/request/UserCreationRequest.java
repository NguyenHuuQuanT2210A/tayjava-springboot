package com.example.backendservice.controller.request;

import com.example.backendservice.common.Gender;
import com.example.backendservice.common.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserCreationRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String username;
    private Gender gender;
    private String birthday;
    @Email(message = "Email should be valid")
    private String email;
    private String phone;
    private UserType type;
    private List<AddressRequest> addresses; //home, office
}
