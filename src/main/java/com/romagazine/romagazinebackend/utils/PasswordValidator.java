package com.romagazine.romagazinebackend.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    public void validatePassword(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters long");
        }

        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }

        if (!DIGIT_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }

        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
} 