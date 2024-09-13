package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);
    Account findByEmailAndPassword(String email, String password);
    boolean addRecipeToRecipeLikedList(String recipeId, String accountId); //TODO vérifier si ça marche
    boolean addIngredientToIngredientList(String ingredientId, String accountId);
}
