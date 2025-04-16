package com.example.backendservice.controller;

import com.example.backendservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL_CONTROLLER")
@RequestMapping("/api/v1/email")
public class EmailController {
     private final EmailService emailService;

     @GetMapping("/send")
    public void sendEmail(@RequestParam String to,
                                @RequestParam String subject,
                                @RequestParam String body) {
         log.info("Sending email to: {}, subject: {}, body: {}", to, subject, body);
        emailService.sendEmail(to, subject, body);
    }

    @GetMapping("/verify-email")
    public void verifyEmail(@RequestParam String to,
                                @RequestParam String name) {
        emailService.emailVerification(to, name);
    }
}
