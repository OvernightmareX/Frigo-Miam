package com.example.FrigoMiamBack.interfaces;


import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;

import java.util.List;

public interface IIngredientService {

    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(String id);

    Ingredient addIngredient(Ingredient ingredient);

    boolean deleteIngredient(String id);

    Ingredient updateIngredient(Ingredient ingredient);

//    boolean addIngredientToRecipe(Recipe recipe, Ingredient ingredient);
}
