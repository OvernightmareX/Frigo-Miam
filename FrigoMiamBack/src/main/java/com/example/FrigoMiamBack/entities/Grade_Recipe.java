package com.example.FrigoMiamBack.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade_Recipe {
    @EmbeddedId
    private Grade_Recipe_Id id = new Grade_Recipe_Id();

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private int rate;
}
