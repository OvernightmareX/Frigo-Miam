package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.interfaces.IIngredientService;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService implements IIngredientService {
    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllIngredients() {
        return this.ingredientRepository.findAll();
    }

    @Override
    public boolean addIngredient(Ingredient ingredient) {
        try {
            this.ingredientRepository.save(ingredient);
            return true;
        } catch (Exception e) {
            //TODO faire exception personnalisée
            return false;
        }
    }

    @Override
    public boolean deleteIngredient(Ingredient ingredient) {
        try {
            this.ingredientRepository.delete(ingredient);
            return true;
        } catch (Exception e) {
            //TODO faire exception personnalisée
            return false;
        }
    }

    @Override
    public boolean updateIngredient(Ingredient ingredient) {
        try {
            this.ingredientRepository.save(ingredient);
            return true;
        } catch (Exception e) {
            //TODO faire exception personnalisée
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Ingredient> getFridge(String accountId) {
        return this.ingredientRepository.findAll();
    }
}
