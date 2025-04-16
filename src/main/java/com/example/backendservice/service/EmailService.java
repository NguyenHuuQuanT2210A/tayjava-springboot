package com.example.backendservice.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL_SERVICE")
public class EmailService {

    @Value("${spring.sendgrid.from-email}")
    private String from;

    @Value("${spring.sendgrid.template-id}")
    private String templateId;

    @Value("${spring.sendgrid.verification-link}")
    private String verificationLink;

    private final SendGrid sendGrid;

    /**
     * Sends an email using SendGrid.
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param body    The body of the email.
     */
    public void sendEmail(String to, String subject, String body) {
        // Logic to send email
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("/mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if (response.getStatusCode() != 202) {
                log.error("Failed to send email: {}", response.getBody());
            } else {
                log.info("Email sent successfully to: {}", to);
            }

        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }

        log.info("Sending email to: {}, subject: {}, body: {}", to, subject, body);
    }

    /**
     * Sends an email verification link to the user.
     * @param to   The recipient's email address.
     * @param name The name of the recipient.
     */
    public void emailVerification(String to, String name) {
        log.info("Sending email verification to: {}", to);
        Email fromEmail = new Email(from, "Backend Service");
        Email toEmail = new Email(to);

        String subject = "Email Verification";

        String secretCode = String.format("?secretCode=%s", UUID.randomUUID());

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("verification_link", verificationLink + secretCode);

        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);

        //add to dynamic data
        map.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("/mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            if (response.getStatusCode() != 202) {
                log.error("Failed to send email verification: {}", response.getBody());
            } else {
                log.info("Email verification sent successfully to: {}", to);
            }

        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }
}
