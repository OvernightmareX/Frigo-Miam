package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.LoginRequest;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.utils.constants.ApiUrls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrls.ACCOUNT)
public class AccountController {

    private final IAccountService iAccountService;

    public AccountController(IAccountService iAccountService) {
        this.iAccountService = iAccountService;
    }

    @GetMapping(ApiUrls.EMAIL)
    public ResponseEntity<Boolean> checkEmail(@RequestParam @NotBlank String email) {
        return new ResponseEntity<>(this.iAccountService.checkEmail(email.toLowerCase().trim()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Boolean> createAccount(@Valid @RequestBody Account accountToCreate) {
        return new ResponseEntity<>(this.iAccountService.createAccount(accountToCreate), HttpStatus.CREATED);
    }

    @PostMapping(ApiUrls.LOGIN)
    public ResponseEntity<Boolean> logIn(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return new ResponseEntity<>(this.iAccountService.logIn(email, password), HttpStatus.OK);
    }
}