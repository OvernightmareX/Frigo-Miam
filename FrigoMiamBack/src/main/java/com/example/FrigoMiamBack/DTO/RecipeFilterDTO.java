package com.example.FrigoMiamBack.DTO;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilterDTO {
    private List<Ingredient> ingredientList;
    private List<Allergy> allergyList;
    private Diet diet;
}
