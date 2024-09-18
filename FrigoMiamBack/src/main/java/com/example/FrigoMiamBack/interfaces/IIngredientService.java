package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;

import java.util.List;
import java.util.UUID;

public interface IIngredientService {

    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(UUID id);

    Ingredient addIngredient(Ingredient ingredient);

    boolean deleteIngredient(UUID id);

    Ingredient updateIngredient(Ingredient ingredient);

//    boolean addIngredientToRecipe(Recipe recipe, Ingredient ingredient);
}
