package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.TypeIngredient;
import com.example.FrigoMiamBack.utils.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Unit unit;
    private TypeIngredient typeIngredient;
    private Allergy allergy;

    @OneToMany(mappedBy = "ingredient")
    private List<Fridge> accountIngredientsList;

    @OneToMany(mappedBy = "ingredient")
    private List<Recipe_Ingredient> recipeIngredientsList;
}
