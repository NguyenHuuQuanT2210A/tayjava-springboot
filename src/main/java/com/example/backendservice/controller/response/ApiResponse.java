package com.example.backendservice.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ApiResponse implements Serializable {
    private int status;
    private String message;
    private Object data;
}
