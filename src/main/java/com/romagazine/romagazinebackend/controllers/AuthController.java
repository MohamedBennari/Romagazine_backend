package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.enums.Status;
import com.romagazine.romagazinebackend.services.FileStorageService;
import com.romagazine.romagazinebackend.services.LoginAttemptService;
import com.romagazine.romagazinebackend.services.UserService;
import com.romagazine.romagazinebackend.utils.ImageUtils;
import com.romagazine.romagazinebackend.utils.JwtTokenUtil;
import com.romagazine.romagazinebackend.utils.PasswordValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");

        if (loginAttemptService.isBlocked(username)) {
            long remainingTime = loginAttemptService.getRemainingBlockTime(username);
            logger.warn("Tentative de connexion pour un compte bloqué: {}", username);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Account is blocked",
                    "message", "Please try again in " + (remainingTime / 60000) + " minutes"
            ));
        }

        try {
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            logger.info("DEBUG USER: username={}, status='{}'", user.getUsername(), user.getStatus());
            if (user.getStatus() != Status.APPROVED) {
                logger.info("Connexion refusée: statut utilisateur non approuvé ({}), username: {}", user.getStatus(), username);
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Account not approved",
                        "message", "Your account status is " + user.getStatus() + ". Please check your email."
                ));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            loginRequest.get("password")
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            loginAttemptService.resetAttempts(username);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());

            logger.info("Connexion réussie pour l'utilisateur: {}", username);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            loginAttemptService.recordFailedAttempt(username);
            logger.error("Utilisateur non trouvé: {}", username);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "User not found",
                    "message", e.getMessage()
            ));
        } catch (BadCredentialsException e) {
            loginAttemptService.recordFailedAttempt(username);
            logger.warn("Mot de passe incorrect pour l'utilisateur: {}", username);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid credentials",
                    "message", "Invalid username or password"
            ));
        } catch (Exception e) {
            loginAttemptService.recordFailedAttempt(username);
            logger.error("Erreur technique lors de la connexion pour {}: {}", username, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Login error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            passwordValidator.validatePassword(user.getPassword());
            User registeredUser = userService.createUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok().body("Email verified successfully. You can now login.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> getResetPasswordPage(@RequestParam String token) {
        try {
            // Verify token is valid
            userService.validateResetToken(token);
            return ResponseEntity.ok().body("Token is valid. Please proceed with password reset.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String token = request.get("token");

        try {
            passwordValidator.validatePassword(newPassword);
            userService.resetPassword(email, newPassword, token);
            return ResponseEntity.ok().body("Password reset successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok().body(Map.of("message", "Password reset instructions sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @Operation(summary = "Upload user profile picture", description = "Upload a profile picture for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{id}/upload-profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> uploadProfilePicture(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            if (!ImageUtils.isValidImage(file)) {
                return ResponseEntity.badRequest().build();
            }

            String filename = fileStorageService.storeFile(file);
            String imageUrl = ImageUtils.generateImageUrl(filename);

            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setProfilePicture(imageUrl);
            // FIX: Save the user directly, do not call updateUser with a partial object
            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
    }
}
