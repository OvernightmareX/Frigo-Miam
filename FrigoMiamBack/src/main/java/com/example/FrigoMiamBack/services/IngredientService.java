package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IIngredientService;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IngredientService implements IIngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
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
            throw new ConflictException(ExceptionsMessages.ACCOUNT_ALREADY_CREATED, HttpStatus.CONFLICT, LocalDateTime.now());
        }

        try {
            return this.ingredientRepository.save(ingredient);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteIngredient(Ingredient ingredient) {
        if(ingredient.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!ingredientRepository.existsById(ingredient.getId())){
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            this.ingredientRepository.delete(ingredient);
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
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            return this.ingredientRepository.save(ingredient);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Ingredient> getFridge(String accountId) {
        return this.ingredientRepository.findAll();
    }

    @PostConstruct
    private void loadJSONIngredient() throws IOException {
        //TODO remove this function when we will be in production.
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Ingredient>> typeReference = new TypeReference<>(){};
        Path filePath = new ClassPathResource("ingredients.json").getFile().toPath();
        String json = Files.readString(filePath);

        try {
            List<Ingredient> ingredients = objectMapper.readValue(json, typeReference);
            this.ingredientRepository.saveAll(ingredients);
            System.out.println(ingredients);
            System.out.println("Ingredients saved!");
        } catch (IOException e) {
            System.out.println("Unable to save questions: " + e.getMessage());
        }
    }
}
