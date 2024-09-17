package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;

import java.util.List;

public interface IRecipeService {
    Recipe findByID(String id);
    List<Recipe> findAll();
    Recipe addRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    boolean deleteRecipe(String id);

    List<Recipe> getFavoriteRecipes(String accountId);

    List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, Diet diets);
//
//    boolean addGradeToRecipe(String recipeId, String accountId, int grade);
//    int getAverageGrade(String recipeId);
//
//    int getAccountGrade(String recipeId, String accountId);
//

//
//    List<Recipe> getRecipeCreated(String accountId);
}
