package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;

import java.util.List;
import java.util.UUID;

public interface IRecipeService {
    Recipe findByID(UUID id);
    List<Recipe> findAll();
    Recipe addRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    boolean deleteRecipe(UUID id);
    boolean existsById(UUID id);

    List<Recipe> getFavoriteRecipes(UUID accountId);

    //FINIR CETTE METHODE
    List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, Diet diets);

    boolean addGradeToRecipe(Recipe Recipe, Account Account, int grade);
    int getAverageGrade(String recipeId);

    int getAccountGrade(String recipeId, String accountId);

//    List<Recipe> getRecipeCreated(String accountId);
}
