package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.DTO.TokenDTO;
import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.*;
import com.example.FrigoMiamBack.utils.HashingUtils;
import com.example.FrigoMiamBack.utils.JwtUtils;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public AccountService(AccountRepository accountRepository, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.accountRepository = accountRepository;
        this.recipeRepository = recipeRepository;
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

        accountToCreate.setPassword(HashingUtils.hashPassword(accountToCreate.getPassword()));

        if(accountToCreate.getRole() == null)
            accountToCreate.setRole(Role.USER);

        return this.accountRepository.save(accountToCreate);
    }

    @Override
    public List<Account> getAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccountByToken(String token) {
        Account accountFound = this.accountRepository.findByEmail(JwtUtils.extractEmail(token));
        if(accountFound == null)
            throw new NotFoundException(ExceptionsMessages.EMAIL_IN_TOKEN_NOT_VALID, HttpStatus.NOT_FOUND, LocalDateTime.now());

        if(JwtUtils.validateToken(token, accountFound))
            return accountFound;
        else
            throw new NotFoundException(ExceptionsMessages.TOKEN_NULL_CANNOT_VALIDATE_TOKEN, HttpStatus.BAD_REQUEST, LocalDateTime.now());

    }

    @Override
    public Account getAccountById(UUID accountId) {
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ID_CANNOT_FIND_ACCOUNT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        return this.accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public TokenDTO logIn(String email, String password) {
        Account accountFound = this.accountRepository.findByEmail(email);

        if(accountFound == null)
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_TO_LOGIN_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());

        if(HashingUtils.verifyPassword(password, accountFound.getPassword()))
            return new TokenDTO(JwtUtils.generateToken(accountFound, Role.USER));
        else
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_TO_LOGIN_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
    }



    @Override
    public Account updateAccount(Account accountToUpdate) {
        log.info("updateAccount:: update account with id {}", accountToUpdate.getId());

        if (accountToUpdate.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ID_CANNOT_UPDATE_ACCOUNT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (!checkEmail(accountToUpdate.getEmail())) {
            throw new NotFoundException(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_UPDATE, HttpStatus.NOT_FOUND, LocalDateTime.now());
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
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ID_CANNOT_DELETE_ACCOUNT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (!this.accountRepository.existsById(accountToDelete.getId())) {
            throw new NotFoundException(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_DELETE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            this.accountRepository.delete(accountToDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Account addRecipeToFavorite(Account account, Recipe recipe) {
        if (account.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_ADD_RECIPE_TO_FAVORITE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        if (recipe.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_ADD_RECIPE_TO_FAVORITE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(recipe.getId())) {
            throw new NotFoundException(ExceptionsMessages.NO_RECIPE_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE, HttpStatus.NOT_FOUND, LocalDateTime.now());
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
        log.info("addRecipeToFavorite:: add an ingredient {} with a quantity of {} for account with id {}", ingredient.getName(), quantity ,account.getId());
        if (quantity <= 0) {
            throw new WrongParameterException(ExceptionsMessages.QUANTITY_CANNOT_BE_ZERO_OR_LESS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if (account.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(account.getId())) {
            throw new NotFoundException(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if (ingredient.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_INGREDIENT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.ingredientRepository.existsById(ingredient.getId())) {
            throw new NotFoundException(ExceptionsMessages.NO_INGREDIENT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE, HttpStatus.NOT_FOUND, LocalDateTime.now());
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
                    throw new ConflictException(ExceptionsMessages.INGREDIENT_ALREADY_ADDED_TO_FRIDGE, HttpStatus.CONFLICT, LocalDateTime.now());
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
    public List<Fridge> getFridges(UUID accountId) {
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_FIND_FRIDGE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.accountRepository.existsById(accountId)) {
            throw new NotFoundException(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_FIND_FRIDGE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        try {
            return this.getAccountById(accountId).getAccountIngredientsList();
        } catch (Exception e) {
            return null;
        }
    }
}
