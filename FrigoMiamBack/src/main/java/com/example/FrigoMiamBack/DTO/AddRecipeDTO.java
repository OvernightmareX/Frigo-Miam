package com.example.FrigoMiamBack.DTO;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeDTO {
    private Recipe recipe;
    private Account account;
    private List<IngredientQuantityDTO> ingredients;
}
