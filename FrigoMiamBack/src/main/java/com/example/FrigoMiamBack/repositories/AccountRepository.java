package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);
}
