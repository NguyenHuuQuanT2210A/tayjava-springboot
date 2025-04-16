package com.example.backendservice.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    private String username;
    private String password;
    private String platform;
    private String deviceToken;
    private String versionApp;

}
