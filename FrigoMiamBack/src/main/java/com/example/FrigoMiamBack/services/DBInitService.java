package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.DTO.IngredientQuantityDTO;
import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeIngredientRepository;
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
    private final RecipeIngredientRepository recipeIngredientRepository;

    public DBInitService(AccountService accountService,
                         RecipeRepository recipeRepository,
                         RecipeService recipeService,
                         IngredientRepository ingredientRepository,
                         RecipeIngredientRepository recipeIngredientRepository) throws IOException {
        this.accountService = accountService;
        this.recipeService = recipeService;
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;

        initDB();
    }


    private void initDB() throws IOException {

        List<Ingredient> ingredients = saveAndFetchIngredients();

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


        this.accountService.createAccount(account);

        admin.setRecipeCreatedList(new ArrayList<>());
        Account adminCreated = this.accountService.createAccount(admin);

        List<Recipe> recipes = loadJSONRecipe();

        List<IngredientQuantityDTO> allIngredients = new ArrayList<>();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(0),2));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(13),50));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(12),10));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(50),5));
        recipeService.addRecipe(recipes.get(0), adminCreated, allIngredients);


        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(7),200));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(10),100));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(11),5));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(5),10));
        recipeService.addRecipe(recipes.get(1), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(1),300));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(44),1));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(13),20));
        recipeService.addRecipe(recipes.get(2), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(6),200));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(11),10));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(19),5));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(13),20));
        recipeService.addRecipe(recipes.get(3), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(37),200));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(11),5));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(12),10));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(9),20));
        recipeService.addRecipe(recipes.get(4), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(16),2));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(22),1));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(4),2));
        recipeService.addRecipe(recipes.get(5), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(25),300));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(28),150));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(17),30));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(12),10));
        recipeService.addRecipe(recipes.get(6), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(10),150));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(59),100));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(12),10));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(5),5));
        recipeService.addRecipe(recipes.get(7), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(35),300));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(11),5));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(14),20));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(47),5));
        recipeService.addRecipe(recipes.get(8), adminCreated, allIngredients);

        allIngredients.clear();
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(0),200));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(8),100));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(2),50));
        allIngredients.add(new IngredientQuantityDTO( ingredients.get(17),30));
        recipeService.addRecipe(recipes.get(9), adminCreated, allIngredients);

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

    private List<Ingredient> saveAndFetchIngredients() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/ingredients.json");

        if (inputStream == null) {
            throw new IOException("File not found: ingredients.json");
        }

        List<Ingredient> ingredients;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ObjectMapper objectMapper = new ObjectMapper();
            ingredients = objectMapper.readValue(reader, new TypeReference<List<Ingredient>>() {});
        }

        // Save ingredients and re-fetch to ensure they are managed
        ingredientRepository.saveAll(ingredients);

        // Fetch them from the database to ensure they are managed
        return ingredientRepository.findAll();
    }
}
