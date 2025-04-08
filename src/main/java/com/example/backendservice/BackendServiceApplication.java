package com.example.backendservice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendServiceApplication {

    @Value("${jwt.secret}")
    private String jwtKey;

    public static void main(String[] args) {
        SpringApplication.run(BackendServiceApplication.class, args);


    }

    @PostConstruct
    public void init() {
        System.out.println("JWT Key: " + jwtKey);
    }
}
