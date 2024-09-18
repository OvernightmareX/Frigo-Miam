package com.example.FrigoMiamBack.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtils {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
