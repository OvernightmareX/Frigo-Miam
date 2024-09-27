package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for this test
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountService iAccountService;

    @Test
    public void testGetAccounts() throws Exception {
        List<Account> accounts = Arrays.asList(
                AccountFactory.createAccountWithId(UUID.randomUUID()),
                AccountFactory.createAccountWithId(UUID.randomUUID())
        );

        when(iAccountService.getAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/account/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("email@test.com"))
                .andExpect(jsonPath("$[1].email").value("email@test.com"))
                .andDo(print());

        verify(iAccountService, times(1)).getAccounts();
    }

    @Test
    public void testGetAccountById() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Account account = AccountFactory.createAccountWithId(accountId);

        when(iAccountService.getAccountById(accountId)).thenReturn(account);

        // Act & Assert
        mockMvc.perform(get("/account/{id}", accountId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andDo(print());

        verify(iAccountService, times(1)).getAccountById(accountId);
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account accountToCreate = AccountFactory.createAccountWithId(UUID.randomUUID());

        when(iAccountService.createAccount(any(Account.class))).thenReturn(accountToCreate);

        // Act & Assert
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"firstname\": \"firstname\",\n" +
                                "  \"lastname\": \"lastname\",\n" +
                                "  \"email\": \"email@test.com\",\n" +
                                "  \"password\": \"password\"\n" +
                                "}")) // Example JSON body with required fields
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value(AccountFactory.DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastname").value(AccountFactory.DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.email").value(AccountFactory.DEFAULT_EMAIL))
                .andExpect(jsonPath("$.password").value(AccountFactory.DEFAULT_PASSWORD))
                .andDo(print());

        verify(iAccountService, times(1)).createAccount(any(Account.class));
    }
}
