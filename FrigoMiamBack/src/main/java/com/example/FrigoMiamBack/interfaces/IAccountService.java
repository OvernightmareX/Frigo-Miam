package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Account;

import java.util.List;

public interface IAccountService {

    boolean checkEmail(String email);

    boolean logIn(String email, String password);

    Account createAccount(Account accountToCreate);

    List<Account> getAccounts();

    Account getAccount(String accountId);

    boolean updateAccount(Account accountToUpdate);

    boolean deleteAccount(String email, String password);

    boolean addRecipeToFavorite(String accountId, String recipeId);

    boolean addIngredientToFridge(String ingredientId, String accountId);

}
