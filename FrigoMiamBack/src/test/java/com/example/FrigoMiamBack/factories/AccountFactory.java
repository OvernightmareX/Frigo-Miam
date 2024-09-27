package com.example.FrigoMiamBack.factories;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.utils.enums.Role;

import java.util.UUID;

public class AccountFactory {
    public static final String DEFAULT_FIRSTNAME = "firstname";
    public static final String DEFAULT_LASTNAME = "lastname";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_EMAIL = "email@test.com";

    public static Account createDefaultAccount() {
        return Account.builder()
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME)
                .password(DEFAULT_PASSWORD)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static Account createAccountWithRole(Role role) {
        return Account.builder()
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME)
                .password(DEFAULT_PASSWORD)
                .email(DEFAULT_EMAIL)
                .role(role)
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
