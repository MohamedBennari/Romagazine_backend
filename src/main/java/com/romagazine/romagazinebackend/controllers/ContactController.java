package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private EmailService emailService;


    @PostMapping
    public ResponseEntity<?> sendContactMessage(@RequestBody Map<String, String> request,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String nom = request.get("nom");
        String prenom = request.get("prenom");
        String email = request.get("email");
        String objet = request.get("objet");
        String message = request.get("message");
        // Adresse du destinataire (configurée dans application.properties)
        String destinataire = fromEmail; // ou injectée via @Value
        emailService.sendContactEmail(destinataire, nom, prenom, email, objet, message);
        logger.info("Message de contact reçu: email={}, objet={}, message={}", email, objet, message);
        // Ici, tu peux ajouter l'envoi d'email au destinataire configuré
        return ResponseEntity.ok().body(Map.of("message", "Votre message a été reçu. Merci de nous avoir contactés !"));
    }
}
