package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean findByEmail(String email);
    Account findByEmailAndPassword(String email, String password);
}
