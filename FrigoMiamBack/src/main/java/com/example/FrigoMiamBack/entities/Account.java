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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "account",  cascade = CascadeType.ALL)
    private List<Fridge> accountIngredientsList;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Recipe> recipeCreatedList;

    @OneToMany(mappedBy = "account")
    private List<Grade_Recipe> accountRecipeList;

    @ManyToMany
    @JoinTable(name = "account_likes_recipe",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipeLikedList;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", allergies=" + (allergies != null ? allergies : "none") +
                ", diets=" + (diets != null ? diets : "none") +
                ", role=" + (role != null ? role.getName() : "none") +
                ", accountIngredientsListSize=" + (accountIngredientsList != null ? accountIngredientsList.size() : 0) +
                ", recipeCreatedListSize=" + (recipeCreatedList != null ? recipeCreatedList.size() : 0) +
                ", accountRecipeListSize=" + (accountRecipeList != null ? accountRecipeList.size() : 0) +
                ", recipeLikedListSize=" + (recipeLikedList != null ? recipeLikedList.size() : 0) +
                '}';
    }

}
