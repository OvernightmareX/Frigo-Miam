package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_recipe;
    private String instructions;
    private int preparation_time;
    private int cooking_time;
    private int calories;
    private TypeRecipe typeRecipe;
    private Validation validation;
    private Diet diet;

    @OneToMany(mappedBy = "recipe")
    private List<Recipe_Ingredient> recipeIngredientsList;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "recipe")
    private List<Account_Recipe> accountRecipeList;

    @ManyToMany(mappedBy = "recipeLikedList")
    private List<Account> accountList;
}
