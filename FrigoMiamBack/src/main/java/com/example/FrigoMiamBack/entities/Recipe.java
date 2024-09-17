package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private String instructions;
    private int preparation_time;
    private int cooking_time;
    private int calories;
    private TypeRecipe typeRecipe;
    private Validation validation;
    private Diet diet;

    @OneToMany(mappedBy = "recipe")
    @Builder.Default
    private List<Recipe_Ingredient> recipeIngredientsList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "recipe")
    @Builder.Default
    private List<Grade_Recipe> accountRecipeList = new ArrayList<>();

    @ManyToMany(mappedBy = "recipeLikedList")
    @Builder.Default
    private List<Account> accountList = new ArrayList<>();
}
