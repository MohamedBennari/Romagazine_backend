package com.romagazine.romagazinebackend.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPT = 5;
    private static final long BLOCK_DURATION = 15 * 60 * 1000; // 15 minutes in milliseconds

    private final Map<String, AtomicInteger> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedUsers = new ConcurrentHashMap<>();

    public void recordFailedAttempt(String username) {
        AtomicInteger attempts = attemptsCache.computeIfAbsent(username, k -> new AtomicInteger(0));
        int currentAttempts = attempts.incrementAndGet();

        if (currentAttempts >= MAX_ATTEMPT) {
            blockedUsers.put(username, System.currentTimeMillis() + BLOCK_DURATION);
        }
    }

    public void resetAttempts(String username) {
        attemptsCache.remove(username);
        blockedUsers.remove(username);
    }

    public boolean isBlocked(String username) {
        Long blockEndTime = blockedUsers.get(username);
        if (blockEndTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > blockEndTime) {
            blockedUsers.remove(username);
            attemptsCache.remove(username);
            return false;
        }

        return true;
    }

    public long getRemainingBlockTime(String username) {
        Long blockEndTime = blockedUsers.get(username);
        if (blockEndTime == null) {
            return 0;
        }
        return Math.max(0, blockEndTime - System.currentTimeMillis());
    }
} 