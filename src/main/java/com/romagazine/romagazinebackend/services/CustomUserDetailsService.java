package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.enums.Status;
import com.romagazine.romagazinebackend.exceptions.UserNotApprovedException;
import com.romagazine.romagazinebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Check if user is approved
        if (!Status.APPROVED.name().equals(user.getStatus())) {
            throw new UserNotApprovedException("Your account is not approved. Please verify your email first.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
} 