package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Ingredient;

import java.util.List;

public interface IIngredientService {

    List<Ingredient> getAllIngredients();

    boolean addIngredient(Ingredient ingredient);

    boolean deleteIngredient(Ingredient ingredient);

    boolean updateIngredient(Ingredient ingredient);

    List<Ingredient> getFridge(String accountId);
}
