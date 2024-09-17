package com.example.FrigoMiamBack.entities;

import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    private LocalDate birthdate;
    private Allergy allergies;
    private Diet diets;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "account",  cascade = CascadeType.ALL)
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
