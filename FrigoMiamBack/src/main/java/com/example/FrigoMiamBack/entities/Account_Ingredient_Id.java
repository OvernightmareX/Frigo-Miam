package com.example.FrigoMiamBack.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account_Ingredient_Id implements Serializable {
    private UUID accountId;
    private UUID ingredientId;
}
