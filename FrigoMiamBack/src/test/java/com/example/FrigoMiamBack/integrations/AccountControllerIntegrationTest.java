package com.example.FrigoMiamBack.integrations;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Resets database for each test
public class AccountControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/account";
    }
    @Test
    public void testCreateAccount() {
        Account newAccount = new Account();
        newAccount.setEmail("test@test.com");
        newAccount.setPassword("password123");
        newAccount.setFirstname("John");
        newAccount.setLastname("Doe");

        ResponseEntity<Account> response = restTemplate.postForEntity(baseUrl, newAccount, Account.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testCheckEmail() {
        Account account = new Account();
        account.setEmail("existing@test.com");
        account.setPassword("password");
        account.setFirstname("Existing");
        account.setLastname("User");
        accountRepository.save(account);

        String url = baseUrl + "/email?email=existing@test.com";
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

}
