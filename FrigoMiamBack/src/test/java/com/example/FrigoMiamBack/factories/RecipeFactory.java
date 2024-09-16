package com.example.FrigoMiamBack.factories;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.FrigoMiamBack.utils.enums.Diet.VEGAN;
import static com.example.FrigoMiamBack.utils.enums.Validation.VALIDATED;

public class RecipeFactory {
    private static final String DEFAULT_TITLE = "title";
    private static final String DEFAULT_DESCRIPTION = "description";
    private static final String DEFAULT_INSTRUCTIONS = "instructions";
    private static final int DEFAULT_PREPARATION_TIME = 60;
    private static final int DEFAULT_COOKING_TIME = 30;
    private static final int DEFAULT_CALORIES = 150;
    private static final TypeRecipe DEFAULT_TYPE_RECIPE = TypeRecipe.STARTER;
    private static final Validation DEFAULT_VALIDATION = VALIDATED;
    private static final Diet DEFAULT_DIET = VEGAN;


    public static Recipe createDefaultRecipe() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Title");
        recipe.setDescription("Description");
        recipe.setInstructions("Instructions");
        recipe.setPreparation_time(10);
        recipe.setCooking_time(20);
        recipe.setCalories(30);
        recipe.setTypeRecipe(TypeRecipe.MAIN_COURSE);
        recipe.setValidation(Validation.INVALIDATED);
        recipe.setDiet(Diet.VEGETARIAN);
        recipe.setRecipeIngredientsList(new ArrayList<>());
        recipe.setAccountRecipeList(new ArrayList<>());
        recipe.setAccountList(new ArrayList<>());
        return recipe;
    }

    public static Recipe createCustomRecipe(String title, String description, String instructions, int prepTime, int cookTime, int calories, TypeRecipe typeRecipe, Validation validation, Diet diet, Account account) {
        Recipe recipe = new Recipe();
        recipe.setId_recipe(UUID.randomUUID());
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setInstructions(instructions);
        recipe.setPreparation_time(prepTime);
        recipe.setCooking_time(cookTime);
        recipe.setCalories(calories);
        recipe.setTypeRecipe(typeRecipe);
        recipe.setValidation(validation);
        recipe.setDiet(diet);
        recipe.setAccount(account);
        recipe.setRecipeIngredientsList(new ArrayList<>());
        recipe.setAccountRecipeList(new ArrayList<>());
        recipe.setAccountList(new ArrayList<>());
        return recipe;
    }

    public static Recipe createRecipeWithId(UUID id){
        return Recipe.builder()
                .id_recipe(id)
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .instructions(DEFAULT_INSTRUCTIONS)
                .preparation_time(DEFAULT_PREPARATION_TIME)
                .cooking_time(DEFAULT_COOKING_TIME)
                .calories(DEFAULT_CALORIES)
                .typeRecipe(DEFAULT_TYPE_RECIPE)
                .validation(DEFAULT_VALIDATION)
                .diet(DEFAULT_DIET)
                .build();
    }
}
