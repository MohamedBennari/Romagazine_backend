package com.romagazine.romagazinebackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("token", token);
            context.setVariable("verificationUrl", "http://localhost:8081/api/auth/verify-email?token=" + token);
            
            String emailContent = templateEngine.process("email/verification-email", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verify your email address");
            helper.setText(emailContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("token", token);
            context.setVariable("resetUrl", "http://localhost:8081/api/auth/reset-password?token=" + token);
            
            String emailContent = templateEngine.process("email/password-reset-email", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Reset your password");
            helper.setText(emailContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(String to, String username) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            
            String emailContent = templateEngine.process("email/welcome-email", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Welcome to Romagazine!");
            helper.setText(emailContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }
} 