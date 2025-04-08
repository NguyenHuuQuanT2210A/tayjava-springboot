package com.example.backendservice.controller.response;

import com.example.backendservice.common.Gender;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Gender gender;
    private String birthday;
    private String email;
    private String phone;
    // more
}
