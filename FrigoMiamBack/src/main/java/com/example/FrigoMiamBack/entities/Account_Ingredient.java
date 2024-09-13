package com.example.FrigoMiamBack.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account_Ingredient {

    @EmbeddedId
    private Account_Ingredient_Id id = new Account_Ingredient_Id();

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "user_id")
    private Account account;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private double quantity;
}
