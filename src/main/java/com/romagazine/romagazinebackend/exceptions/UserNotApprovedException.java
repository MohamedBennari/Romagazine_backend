package com.romagazine.romagazinebackend.exceptions;
 
public class UserNotApprovedException extends RuntimeException {
    public UserNotApprovedException(String message) {
        super(message);
    }
} 