package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.entities.Role;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBInitService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final AccountService accountService;
    private final RecipeService recipeService;

    public DBInitService(AccountService accountService, RecipeRepository recipeRepository, RecipeService recipeService, IngredientRepository ingredientRepository) throws IOException {
        this.accountService = accountService;
        this.recipeService = recipeService;
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;

        initDB();
    }


    private void initDB() throws IOException {

        List<Ingredient> ingredients = loadJSONIngredient();

        Account account = Account.builder()
                .firstname("first")
                .lastname("last")
                .email("email@test.com")
                .password("password")
                .role(Role.builder().name("USER").build())
                .build();

        Account admin = Account.builder()
                .firstname("admin")
                .lastname("admin")
                .email("admin@admin.com")
                .password("myadminpassword")
                .role(Role.builder().name("ADMIN").build())
                .build();


        Account accountCreated = this.accountService.createAccount(account);
        admin.setRecipeCreatedList(new ArrayList<>());
        Account adminCreated = this.accountService.createAccount(admin);

        List<Recipe> recipes = loadJSONRecipe();

        for(Recipe recipe : recipes){
            recipe.setAccount(admin);
            adminCreated.getRecipeCreatedList().add(recipe);
        }
        accountService.updateAccount(adminCreated);


    }

    private List<Recipe> loadJSONRecipe() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/recipe.json");

        if (inputStream == null) {
            throw new IOException("File not found: recipe.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(reader, new TypeReference<List<Recipe>>() {});
        }
    }

    private List<Ingredient> loadJSONIngredient() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/ingredients.json");

        if (inputStream == null) {
            throw new IOException("File not found: ingredients.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Ingredient> ingredients = objectMapper.readValue(reader, new TypeReference<List<Ingredient>>() {});
            ingredientRepository.saveAll(ingredients);
            return ingredients;
        }
    }
}
