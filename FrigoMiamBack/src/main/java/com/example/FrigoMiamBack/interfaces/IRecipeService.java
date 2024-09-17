package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Recipe;

import java.util.List;
import java.util.UUID;

public interface IRecipeService {
    Recipe findByID(UUID id);
    List<Recipe> findAll();
    Recipe addRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    boolean deleteRecipe(UUID id);
    boolean existsById(UUID id);

//    List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, List<Diet> diets);
//
//    int getAverageGrade(String recipeId);
//
//    int getAccountGrade(String recipeId, String accountId);
//
//    boolean addGradeToRecipe(String recipeId, String accountId, int grade);
//
//    List<Recipe> getFavoriteRecipes(String accountId);
//
//    List<Recipe> getRecipeCreated(String accountId);
}
