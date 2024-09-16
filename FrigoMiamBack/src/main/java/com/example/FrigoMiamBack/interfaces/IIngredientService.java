package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Ingredient;

import java.util.List;

public interface IIngredientService {

    List<Ingredient> getAllIngredients();

    Ingredient getIngredientById(String id);

    Ingredient addIngredient(Ingredient ingredient);

    boolean deleteIngredient(String id);

    Ingredient updateIngredient(Ingredient ingredient);

    List<Ingredient> getFridge(String accountId);
}
