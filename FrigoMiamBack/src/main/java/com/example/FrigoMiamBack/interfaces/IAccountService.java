package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;

import java.util.List;
import java.util.UUID;

public interface IAccountService {

    boolean checkEmail(String email);

    boolean logIn(String email, String password);

    Account createAccount(Account accountToCreate);

    List<Account> getAccounts();

    Account getAccountById(UUID accountId);

    Account updateAccount(Account accountToUpdate);

    boolean deleteAccount(Account accountToDelete);

    Account addRecipeToFavorite(Account account, Recipe recipe);

    boolean addIngredientToFridge(Ingredient ingredient, Account account, int quantity);

    List<Fridge> getFridges(UUID accountId);

}
