package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    @Autowired
    protected UserService userService;

    protected User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
} 