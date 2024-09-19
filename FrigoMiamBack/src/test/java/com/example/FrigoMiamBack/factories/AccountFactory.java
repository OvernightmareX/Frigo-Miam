package com.example.FrigoMiamBack.factories;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Role;

import java.util.UUID;

public class AccountFactory {
    private static final String DEFAULT_FIRSTNAME = "firstname";
    private static final String DEFAULT_LASTNAME = "lastname";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_EMAIL = "email@test.com";

    public static Account createDefaultAccount() {
        return Account.builder()
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME)
                .password(DEFAULT_PASSWORD)
                .email(DEFAULT_EMAIL)
                .role(Role.builder().name("USER").build())
                .build();
    }

    public static Account createAccountWithEmail(String email) {
        return Account.builder()
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME)
                .password(DEFAULT_PASSWORD)
                .email(email)
                .build();
    }

    public static Account createAccountWithId(UUID id) {
        return Account.builder()
                .id(id)
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME)
                .password(DEFAULT_PASSWORD)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static Account createCustomAccount(String firstname, String lastname, String password, String email, UUID id) {
        return Account.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .password(password)
                .email(email)
                .build();
    }
}
