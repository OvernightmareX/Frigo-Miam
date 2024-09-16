package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IIngredientService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IngredientService implements IIngredientService {
    private final AccountRepository accountRepository;
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository, AccountRepository accountRepository) {
        this.ingredientRepository = ingredientRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return this.ingredientRepository.findAll();
    }

    @Override
    public Ingredient getIngredientById(String id) {
        if(id == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        return this.ingredientRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        if(ingredient.getId() != null){
            throw new ConflictException(ExceptionsMessages.INGREDIENT_ALREADY_EXIST, HttpStatus.CONFLICT, LocalDateTime.now());
        }

        try {
            return this.ingredientRepository.save(ingredient);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteIngredient(String id) {
        if(id == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!ingredientRepository.existsById(UUID.fromString(id))){
            throw new NotFoundException(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            this.ingredientRepository.deleteById(UUID.fromString(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        if(ingredient.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!ingredientRepository.existsById(ingredient.getId())){
            throw new NotFoundException(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            return this.ingredientRepository.save(ingredient);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Ingredient> getFridge(Account account) {
        if(account.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!accountRepository.existsById(account.getId())){
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
