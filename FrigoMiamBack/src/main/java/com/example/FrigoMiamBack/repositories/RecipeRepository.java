package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository <Recipe, UUID> {
//    List<Recipe> findRecipesByIngredientAndAllergyAndDiet(List<Ingredient> ingredients, List<Allergy> allergies, List<Diet> diets);
//    List<Integer> findRecipeGrades(UUID id);
//    int findGradeByRecipeAndAccount(UUID recipeId, UUID accountId);
//    boolean addGradeToRecipe(UUID recipeId, UUID accountId, Integer grade);
//    List<Recipe> findRecipeLikedList(UUID accountId);
//    List<Recipe> findrecipeCreatedList(UUID accountId);
}
