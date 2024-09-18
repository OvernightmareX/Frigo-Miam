package com.example.FrigoMiamBack.DTO;

import com.example.FrigoMiamBack.entities.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientQuantityDTO {
    private Ingredient ingredient;
    private int quantity;
}
