package com.example.backendservice.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordRequest {
    @NotNull(message = "Id is required")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
