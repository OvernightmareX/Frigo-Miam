package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Recipe_Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<Recipe_Ingredient, UUID> {
}
