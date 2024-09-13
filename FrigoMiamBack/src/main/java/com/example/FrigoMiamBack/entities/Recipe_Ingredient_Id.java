package com.example.FrigoMiamBack.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe_Ingredient_Id {
    private UUID recipeId;
    private UUID ingredientId;
}
