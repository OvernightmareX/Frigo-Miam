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

    boolean addIngredientToFridge(String ingredientId, String accountId);

    List<Account> getAccounts();

    Account getAccount(String accountId);
}
