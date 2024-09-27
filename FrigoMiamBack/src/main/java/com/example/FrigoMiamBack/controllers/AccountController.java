package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.DTO.AddToFavoriteDTO;
import com.example.FrigoMiamBack.DTO.AddToFridgeDTO;
import com.example.FrigoMiamBack.DTO.LoginRequestDTO;
import com.example.FrigoMiamBack.DTO.TokenDTO;
import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.utils.constants.ApiUrls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiUrls.ACCOUNT)
public class AccountController {

    private final IAccountService iAccountService;

    public AccountController(IAccountService iAccountService) {
        this.iAccountService = iAccountService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(this.iAccountService.getAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable @NotBlank String id) {
        return new ResponseEntity<>(this.iAccountService.getAccountById(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.EMAIL)
    public ResponseEntity<Boolean> checkEmail(@RequestParam @NotBlank String email) {
        return new ResponseEntity<>(this.iAccountService.checkEmail(email.toLowerCase().trim()), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.FRIDGE+"/{id}")
    public ResponseEntity<List<Fridge>> getFridge(@PathVariable @NotBlank String id) {
        return new ResponseEntity<>(this.iAccountService.getFridges(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account accountToCreate) {
        return new ResponseEntity<>(this.iAccountService.createAccount(accountToCreate), HttpStatus.CREATED);
    }

    @PostMapping(ApiUrls.LOGIN)
    public ResponseEntity<TokenDTO> logIn(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(this.iAccountService.logIn(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()), HttpStatus.OK);
    }

    @PostMapping(ApiUrls.PROFIL)
    public ResponseEntity<Account> getAccountByToken(@Valid @RequestBody TokenDTO tokenDTO) {
        return new ResponseEntity<>(this.iAccountService.getAccountByToken(tokenDTO.getToken()), HttpStatus.OK);
    }

    @PostMapping(ApiUrls.FRIDGE)
    public ResponseEntity<Boolean> addIngredientToFridge(@Valid @RequestBody AddToFridgeDTO addToFridgeDTO) {
        return new ResponseEntity<>(this.iAccountService.addIngredientToFridge(
                        addToFridgeDTO.getIngredient(),
                        addToFridgeDTO.getAccount(),
                        addToFridgeDTO.getQuantity()),
                        HttpStatus.OK);
    }

    @PostMapping(ApiUrls.FAVORITE)
    public ResponseEntity<Account> addRecipeToFavorite(@Valid @RequestBody AddToFavoriteDTO addToFavoriteDTO) {
        return new ResponseEntity<>(this.iAccountService.addRecipeToFavorite(addToFavoriteDTO.getAccount(), addToFavoriteDTO.getRecipe()), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody Account accountToUpdate) {
        return new ResponseEntity<>(this.iAccountService.updateAccount(accountToUpdate), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAccount(@Valid @RequestBody Account accountToDelete) {
        return new ResponseEntity<>(this.iAccountService.deleteAccount(accountToDelete), HttpStatus.OK);
    }
}