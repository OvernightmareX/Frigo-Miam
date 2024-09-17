package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
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
    private final RecipeService recipeService;

    public AccountService(AccountRepository accountRepository, RecipeService recipeService) {
        this.accountRepository = accountRepository;
        this.recipeService = recipeService;
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
    public Account getAccountById(UUID accountId) {
        return this.accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public boolean logIn(String email, String password) {
        //TODO vérifier si boolean sur email/password ++ NE PAS TOUCHER
        return this.accountRepository.findByEmailAndPassword(email, password) != null;
    }

    @Override
    public Account updateAccount(Account accountToUpdate) {
        log.info("updateAccount:: update account with id {}", accountToUpdate.getId());
        if (accountToUpdate.getId() == null)
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(!checkEmail(accountToUpdate.getEmail()))
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        try {
            return this.accountRepository.save(accountToUpdate);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteAccount(Account accountToDelete) {
        log.info("deleteAccount:: delete account with id {}", accountToDelete.getId());
        if (accountToDelete.getId() == null)
            throw new WrongParameterException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(!this.accountRepository.existsById(accountToDelete.getId()))
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        try {
            this.accountRepository.delete(accountToDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Account addRecipeToFavorite(Account account, Recipe recipe) {
        log.info("addRecipeToFavorite:: add a recipe {} for account with id {}", recipe.getTitle() ,account.getId());
        if(account.getId() == null)
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(!this.accountRepository.existsById(account.getId()))
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        if(recipe.getId() == null)
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(!this.recipeService.existsById(recipe.getId()))
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        try {
            account.getRecipeLikedList().add(recipe);
            return this.accountRepository.save(account);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addIngredientToFridge(Ingredient ingredient, Account account, int quantity) {
        log.info("addRecipeToFavorite:: add an ingredient {} with a quantity of {} for account with id {}", ingredient.getName(), quantity ,account.getId());
        if(quantity <= 0)
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(account.getId() == null)
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());

        if(!this.accountRepository.existsById(account.getId()))
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        //TODO GERER EXCEPTIONS POUR INGREDIENTS
        //TODO GERER POUR PAS AJOUTER DEUX FOIS LE MEME INGREDIENT

        Fridge fridge = Fridge.builder()
                            .account(account)
                            .ingredient(ingredient)
                            .quantity(quantity)
                            .build();

        account.getAccountIngredientsList().add(fridge);
        try {
            this.accountRepository.save(account);
            log.info("Ingredient {} added to fridge for account {}", ingredient.getName(), account.getEmail());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Fridge> getFridges(UUID accountId) {
        //TODO GERER LES EXCEPTIONS POUR ACCOUNT ID
        return this.getAccountById(accountId).getAccountIngredientsList();
    }
}
