package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.DTO.IngredientQuantityDTO;
import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.entities.Recipe_Ingredient;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;

import java.util.List;
import java.util.UUID;

public interface IRecipeService {
    Recipe findByID(UUID id);
    List<Recipe> findAll();
    Recipe addRecipe(Recipe recipe, Account account, List<IngredientQuantityDTO> ingredients);
    Recipe updateRecipe(Recipe recipe);
    boolean deleteRecipe(UUID id);

    List<Recipe> getFavoriteRecipes(UUID accountId);

    List<Recipe> getPendingRecipes();

    List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, Diet diets);

    boolean addGradeToRecipe(Recipe Recipe, Account Account, int grade);

    int getAverageGrade(UUID recipeId);

    Integer getAccountGrade(UUID recipeId, UUID accountId);

    Recipe addIngredientToRecipe(Recipe recipe, Ingredient ingredient, double quantity);

    List<Recipe> getRecipeCreated(UUID accountId);
}
