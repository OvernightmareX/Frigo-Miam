package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.FridgeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
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
    private final FridgeRepository fridgeRepository;

    public AccountService(AccountRepository accountRepository, FridgeRepository fridgeRepository){
        this.accountRepository = accountRepository;
        this.fridgeRepository = fridgeRepository;
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
            throw new ConflictException(ExceptionsMessages.ACCOUNT_ALREADY_CREATED, HttpStatus.CONFLICT, LocalDateTime.now());

        if(checkEmail(accountToCreate.getEmail()))
            throw new ConflictException(ExceptionsMessages.EMAIL_ALREADY_EXIST, HttpStatus.CONFLICT, LocalDateTime.now());

        return this.accountRepository.save(accountToCreate);
    }

    @Override
    public boolean logIn(String email, String password) {
        //TODO vérifier si boolean sur email/password
        return this.accountRepository.findByEmailAndPassword(email, password) != null;
    }

    @Override
    public Account updateAccount(Account accountToUpdate) {
        log.info("updateAccount:: update account with id {}", accountToUpdate.getId());

        //TODO Ajouter une exceptions si accountToUpdate.Id est null

        try {
            return this.accountRepository.save(accountToUpdate);
        } catch (Exception e) {
            //TODO créer exception personnalisée
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteAccount(Account accountToDelete) {
        //TODO Ajouter une exceptions si accountToDelete.Id est null

        if (accountToDelete != null) {
            this.accountRepository.delete(accountToDelete);
            return true;
        }
        return false;
    }

    @Override
    public Account addRecipeToFavorite(Account account, Recipe recipe) {
        account.getRecipeLikedList().add(recipe);
        return this.accountRepository.save(account);
    }

    @Override
    public boolean addIngredientToFridge(Ingredient ingredient, Account account, int quantity) {

        Fridge_Id fridgeId = new Fridge_Id(account.getId(), ingredient.getId());

        Fridge fridge = Fridge.builder()
                .id(fridgeId)
                .account(account)
                .ingredient(ingredient)
                .quantity(quantity)
                .build();

        try {
            this.fridgeRepository.save(fridge);
            log.info("Ingredient {} added to fridge for account {}", ingredient.getName(), account.getEmail());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Account> getAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccountById(String accountId) {
        return this.accountRepository.findById(UUID.fromString(accountId)).orElse(null);
    }
}
