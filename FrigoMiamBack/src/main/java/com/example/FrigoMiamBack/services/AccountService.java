package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    private AccountRepository accountRepository;

    @Override
    public boolean checkEmail(String email) {
        return this.accountRepository.findByEmail(email) != null;
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
        //TODO vérifier si boolean sur email/password
        return this.accountRepository.findByEmailAndPassword(email, password) != null;
    }

    @Override
    public boolean updateAccount(Account accountToUpdate) {
        try {
            this.accountRepository.save(accountToUpdate);
            return true;
        } catch (Exception e) {
            //TODO créer exception personnalisée
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAccount(String email, String password) {
        Account accountToDelete = this.accountRepository.findByEmailAndPassword(email, password);
        if (accountToDelete != null) {
            this.accountRepository.delete(accountToDelete);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRecipeToFavorite(String accountId, String recipeId) {
        return this.accountRepository.addRecipeToRecipeLikedList(recipeId, accountId);
    }

    @Override
    public boolean addIngredientToFridge(String ingredientId, String accountId) {
      return this.accountRepository.addIngredientToIngredientList(ingredientId, accountId);
    }

    @Override
    public List<Account> getAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccount(String accountId) {
        UUID id = UUID.fromString(accountId);
        if(this.accountRepository.findById(id).isPresent()) {
            return this.accountRepository.findById(id).get();
        }
        return null;
    }
}
