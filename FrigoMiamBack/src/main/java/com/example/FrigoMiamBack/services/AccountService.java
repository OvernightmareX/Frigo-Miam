package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;

import java.util.List;

public class AccountService implements IAccountService {

    private AccountRepository accountRepository;

    @Override
    public boolean checkEmail(String email) {
        return this.accountRepository.findByEmail(email);
    }

    @Override
    public boolean createAccount(Account accountToCreate) {
        try {
            Account createdAccount = this.accountRepository.save(accountToCreate);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
            //TODO rajouter une exception personnalisée
        }
    }

    @Override
    public boolean logIn(String email, String password) {
        Account user = this.accountRepository.findByEmailAndPassword(email, password);

        return false;
    }

    @Override
    public boolean updateAccount(Account accountToUpdate) {
        return false;
    }

    @Override
    public boolean deleteAccount(String email, String password) {
        return false;
    }

    @Override
    public boolean addRecipeToFavorite(String accountId, String recipeId) {
        return false;
    }

    @Override
    public List<Recipe> getFavoriteRecipes(String accountId) {
        return List.of();
    }

    @Override
    public List<Recipe> getRecipeCreated(String accountId) {
        return List.of();
    }

    @Override
    public List<Ingredient> getFridge(String accountId) {
        return List.of();
    }

    @Override
    public boolean addIngredientToFridge(String ingredientId, String accountId) {
        return false;
    }


}
