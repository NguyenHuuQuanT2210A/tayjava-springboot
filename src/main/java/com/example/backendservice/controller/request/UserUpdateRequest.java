package com.example.backendservice.controller.request;

import com.example.backendservice.common.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserUpdateRequest {
    @NotNull(message = "Id is required")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "First name is required")
    private String lastName;
    private String username;
    private Gender gender;
    private String birthday;
    @Email(message = "Email should be valid")
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
