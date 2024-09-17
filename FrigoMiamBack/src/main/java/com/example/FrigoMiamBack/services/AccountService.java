package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.FridgeRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.FrigoMiamBack.utils.constants.ExceptionsMessages.INGREDIENT_ALREADY_ADDED;

@Slf4j
@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final FridgeRepository fridgeRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;

    public AccountService(
            AccountRepository accountRepository,
            FridgeRepository fridgeRepository,
            RecipeRepository recipeRepository,
            IngredientService ingredientService,
            IngredientRepository ingredientRepository) {
        this.accountRepository = accountRepository;
        this.fridgeRepository = fridgeRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
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
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
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

        if (!checkEmail(accountToUpdate.getEmail())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            return this.accountRepository.save(accountToUpdate);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteAccount(Account accountToDelete) {
        log.info("deleteAccount:: delete account with id {}", accountToDelete.getId());

        if (accountToDelete.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (!this.accountRepository.existsById(accountToDelete.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        this.accountRepository.delete(accountToDelete);
        return true;
    }

    @Override
    public Account addRecipeToFavorite(Account account, Recipe recipe) {
        if (account.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        if (recipe.getId_recipe() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(recipe.getId_recipe())) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            account.getRecipeLikedList().add(recipe);
            return this.accountRepository.save(account);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addIngredientToFridge(Ingredient ingredient, Account account, int quantity) {

        if (quantity <= 0) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (account.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if (ingredient.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.ingredientRepository.existsById(ingredient.getId())) {
            throw new NotFoundException(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Fridge fridge = Fridge.builder()
                .account(account)
                .ingredient(ingredient)
                .quantity(quantity)
                .build();

        List<Fridge> fridgeAccount = account.getAccountIngredientsList();

        if (fridgeAccount == null) {
            fridgeAccount = new ArrayList<>();
            fridgeAccount.add(fridge);
            account.setAccountIngredientsList(fridgeAccount);
        } else {
            fridgeAccount.forEach(el -> {
                if(el.getIngredient() == ingredient) {
                    throw new ConflictException(INGREDIENT_ALREADY_ADDED, HttpStatus.CONFLICT, LocalDateTime.now());
                }
            });
            account.getAccountIngredientsList().add(fridge);
        }

        try {
            this.accountRepository.save(account);
            log.info("Ingredient {} added to fridge for account {}", ingredient.getName(), account.getEmail());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Fridge> getFridges(String accountId) {
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(UUID.fromString(accountId))) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        try {
            return this.getAccountById(accountId).getAccountIngredientsList();
        } catch (Exception e) {
            return null;
        }
    }
}
