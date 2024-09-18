package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_recipe;
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
    private List<Recipe_Ingredient> recipeIngredientsList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Grade_Recipe> recipeGradesList;

    @ManyToMany(mappedBy = "recipeLikedList")
    private List<Account> accountList;
}
