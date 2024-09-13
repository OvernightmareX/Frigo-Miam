package com.example.FrigoMiamBack.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fridge {

    @EmbeddedId
    private Fridge_Id id = new Fridge_Id();

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private double quantity;
}
