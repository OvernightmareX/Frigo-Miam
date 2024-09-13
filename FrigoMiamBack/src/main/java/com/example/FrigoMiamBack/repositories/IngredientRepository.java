package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
}
