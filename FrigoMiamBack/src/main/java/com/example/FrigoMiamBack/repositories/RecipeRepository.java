package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository <Recipe, UUID> {
}
