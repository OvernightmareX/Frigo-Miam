package com.example.FrigoMiamBack.factories;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;

import java.util.ArrayList;
import java.util.UUID;

public class RecipeFactory {
    public static Recipe createDefaultRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId_recipe(UUID.randomUUID());
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
}
