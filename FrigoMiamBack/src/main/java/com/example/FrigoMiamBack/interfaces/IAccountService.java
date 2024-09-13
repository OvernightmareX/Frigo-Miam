package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;

import java.util.List;

public interface IAccountService {

    boolean checkEmail(String email);

    boolean createAccount(Account accountToCreate);

    boolean logIn(String email, String password);

    boolean updateAccount(Account accountToUpdate);

    boolean deleteAccount(String email, String password);

    boolean addRecipeToFavorite(String accountId, String recipeId);

    List<Recipe> getFavoriteRecipes(String accountId);

    List<Recipe> getRecipeCreated(String accountId);

    List<Ingredient> getFridge(String accountId);

    boolean addIngredientToFridge(String ingredientId, String accountId);
}
