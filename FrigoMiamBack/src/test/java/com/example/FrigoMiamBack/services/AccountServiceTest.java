package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setup() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void testCheckEmail_WhenEmailExists() {
        String emailSearched = "email@test.com";
        Account account = AccountFactory.createAccountWithEmail(emailSearched);
        accountRepository.save(account);

        assertTrue(accountService.checkEmail(emailSearched));
    }

    @Test
    public void testCheckEmail_WhenEmailNotExists() {
        assertFalse(accountService.checkEmail("emailNotExist@test.fr"));
    }

    @Test
    public void testCreateAccountSuccess() {
        Account account = AccountFactory.createDefaultAccount();
        Account createdAccount = accountService.createAccount(account);

        //TODO add test for other attributes
        assertNotNull(createdAccount.getId());
        assertEquals(account.getFirstname(), createdAccount.getFirstname());
        assertEquals(account.getLastname(), createdAccount.getLastname());
        assertEquals(account.getPassword(), createdAccount.getPassword());
        assertEquals(account.getEmail(), createdAccount.getEmail());
    }

    @Test
    public void testCreateAccountWithExistingEmail() {
        String emailTest = "already@exist.com";
        Account existingAccount = AccountFactory.createAccountWithEmail(emailTest);
        accountRepository.save(existingAccount);

        Account newAccount = AccountFactory.createAccountWithEmail(emailTest);
        ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(newAccount));

        assertEquals("Account with email " + emailTest + " already exists.", thrown.getMessage());
    }

    @Test
    public void testCreateAccountWithId() {
        UUID accountId = UUID.randomUUID();
        Account accountWithId = AccountFactory.createAccountWithId(accountId);

        ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(accountWithId));

        assertEquals("createAccount::account already saved trying to be created.", thrown.getMessage());
    }
}
