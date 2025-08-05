package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.exceptions.UserNotApprovedException;
import com.romagazine.romagazinebackend.services.LoginAttemptService;
import com.romagazine.romagazinebackend.services.UserService;
import com.romagazine.romagazinebackend.utils.JwtTokenUtil;
import com.romagazine.romagazinebackend.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        
        if (loginAttemptService.isBlocked(username)) {
            long remainingTime = loginAttemptService.getRemainingBlockTime(username);
            return ResponseEntity.badRequest().body("Account is blocked. Please try again in " + (remainingTime / 60000) + " minutes");
        }

        try {
            // First, check if the user exists and is approved
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            if (!"APPROVED".equals(user.getStatus())) {
                return ResponseEntity.badRequest().body("Your account is not approved. Please verify your email first.");
            }

            // Then try to authenticate
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

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            loginAttemptService.recordFailedAttempt(username);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BadCredentialsException e) {
            loginAttemptService.recordFailedAttempt(username);
            return ResponseEntity.badRequest().body("Invalid username or password");
        } catch (Exception e) {
            loginAttemptService.recordFailedAttempt(username);
            return ResponseEntity.badRequest().body("An error occurred during login: " + e.getMessage());
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
        return ResponseEntity.ok().body("Logged out successfully");
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
            return ResponseEntity.ok().body("Password reset instructions sent to your email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 