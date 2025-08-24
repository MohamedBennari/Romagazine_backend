package com.romagazine.romagazinebackend.services;
 
public interface EmailService {
    void sendVerificationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
    void sendWelcomeEmail(String to, String username);
    void sendContactEmail(String to, String nom, String prenom, String email, String objet, String message);
} 