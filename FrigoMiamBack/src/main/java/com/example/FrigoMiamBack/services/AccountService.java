package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.FridgeRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
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
//    private final RecipeRepository recipeRepository;

    public AccountService(AccountRepository accountRepository, FridgeRepository fridgeRepository) {
        this.accountRepository = accountRepository;
        this.fridgeRepository = fridgeRepository;
//        this.recipeRepository = recipeRepository();

    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkMail::check email with email {}", email);
        return this.accountRepository.findByEmail(email) != null;
    }

    @Override
    public Account createAccount(Account accountToCreate) {
        log.info("createAccount:: create account with id {}", accountToCreate.getId());
        if (accountToCreate.getId() != null)
            throw new ConflictException(ExceptionsMessages.ACCOUNT_ALREADY_CREATED, HttpStatus.CONFLICT, LocalDateTime.now());

        if (checkEmail(accountToCreate.getEmail()))
            throw new ConflictException(ExceptionsMessages.EMAIL_ALREADY_EXIST, HttpStatus.CONFLICT, LocalDateTime.now());

        return this.accountRepository.save(accountToCreate);
    }

    @Override
    public List<Account> getAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccountById(String accountId) {
        return this.accountRepository.findById(UUID.fromString(accountId)).orElse(null);
    }

    @Override
    public boolean logIn(String email, String password) {
        //TODO vérifier si boolean sur email/password ++ NE PAS TOUCHER
        return this.accountRepository.findByEmailAndPassword(email, password) != null;
    }

    @Override
    public Account updateAccount(Account accountToUpdate) {
        log.info("updateAccount:: update account with id {}", accountToUpdate.getId());

        if (accountToUpdate.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(!checkEmail(accountToUpdate.getEmail())){
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

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
        log.info("deleteAccount:: delete account with id {}", accountToDelete.getId());

        if (accountToDelete.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(!this.accountRepository.existsById(accountToDelete.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        this.accountRepository.delete(accountToDelete);
        return true;
    }

    @Override
    public Account addRecipeToFavorite(Account account, Recipe recipe) {
        if(account.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        //TODO GERER LES EXCEPTIONS EN CAS DE PB AVEC RECIPE (NEED RECIPE REPO)

        account.getRecipeLikedList().add(recipe);
        return this.accountRepository.save(account);
    }

    @Override
    public boolean addIngredientToFridge(Ingredient ingredient, Account account, int quantity) {

        if(quantity <= 0){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(account.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        //TODO GERER EXCEPTIONS POUR INGREDIENTS

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
        } catch (Exception e) {
            //TODO FAIRE EXCEPTION PERSONNALISEE
            return false;
        }
    }

}
