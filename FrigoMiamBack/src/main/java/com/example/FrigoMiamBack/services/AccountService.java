package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkMail::check email with email {}", email);
        return this.accountRepository.findByEmail(email) != null;
    }

    @Override
    public Account createAccount(Account accountToCreate) {
        log.info("createAccount:: create account with id {}", accountToCreate.getId());
        if(accountToCreate.getId() != null)
            throw new ConflictException("createAccount::account already saved trying to be created.", HttpStatus.CONFLICT, LocalDateTime.now());

        if(checkEmail(accountToCreate.getEmail()))
            throw new ConflictException("Account with email " + accountToCreate.getEmail() + " already exists.", HttpStatus.CONFLICT, LocalDateTime.now());

        return this.accountRepository.save(accountToCreate);
    }

    @Override
    public boolean logIn(String email, String password) {
        //TODO vérifier si boolean sur email/password
        return this.accountRepository.findByEmailAndPassword(email, password) != null;
    }

    @Override
    public boolean updateAccount(Account accountToUpdate) {
        try {
            this.accountRepository.save(accountToUpdate);
            return true;
        } catch (Exception e) {
            //TODO créer exception personnalisée
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAccount(String email, String password) {
        Account accountToDelete = this.accountRepository.findByEmailAndPassword(email, password);
        if (accountToDelete != null) {
            this.accountRepository.delete(accountToDelete);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRecipeToFavorite(String accountId, String recipeId) {
        //return this.accountRepository.addRecipeToRecipeLikedList(recipeId, accountId);
        return false;
    }

    @Override
    public boolean addIngredientToFridge(String ingredientId, String accountId) {
        //return this.accountRepository.addIngredientToIngredientList(ingredientId, accountId);
        return false;
    }

    @Override
    public List<Account> getAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccount(String accountId) {
        UUID id = UUID.fromString(accountId);
        if(this.accountRepository.findById(id).isPresent()) {
            return this.accountRepository.findById(id).get();
        }
        return null;
    }
}
