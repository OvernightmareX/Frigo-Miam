package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeRecipe;
import com.example.FrigoMiamBack.utils.enums.Validation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Recipe_Ingredient> recipeIngredientsList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private List<Grade_Recipe> recipeGradesList = new ArrayList<>();

    @ManyToMany(mappedBy = "recipeLikedList")
    @Builder.Default
    @JsonIgnore
    private List<Account> accountList = new ArrayList<>();

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructions='" + instructions + '\'' +
                ", preparation_time=" + preparation_time +
                ", cooking_time=" + cooking_time +
                ", calories=" + calories +
                ", typeRecipe=" + typeRecipe +
                ", validation=" + validation +
                ", diet=" + diet +
                ", recipeIngredientsListSize=" + recipeIngredientsList.size() +
                ", account=" + (account != null ? account.getId() : null) +
                ", recipeGradesListSize=" + recipeGradesList.size() +
                ", accountListSize=" + (accountList != null ? accountList.size() : 0) +
                '}';
    }

}
