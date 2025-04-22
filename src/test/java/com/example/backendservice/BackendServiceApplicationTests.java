package com.example.backendservice;

import com.example.backendservice.controller.AuthenticationController;
import com.example.backendservice.controller.EmailController;
import com.example.backendservice.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendServiceApplicationTests {

    @InjectMocks
    private AuthenticationController authenticationController;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private EmailController emailController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(authenticationController);
        Assertions.assertNotNull(userController);
        Assertions.assertNotNull(emailController);
    }

}
