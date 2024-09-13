package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_account;

    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String email;
    private String password;
    private Allergy allergies;
    private Diet diets;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "account")
    private List<Fridge> accountIngredientsList;

    @OneToMany(mappedBy = "account")
    private List<Recipe> recipeCreatedList;

    @OneToMany(mappedBy = "account")
    private List<Grade_Recipe> accountRecipeList;

    @ManyToMany
    @JoinTable(name = "account_likes_recipe",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipeLikedList;
}
