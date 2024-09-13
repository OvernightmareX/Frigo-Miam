package com.example.FrigoMiamBack.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account_Recipe_Id implements Serializable {
    private UUID accountId;
    private UUID recipeId;
}
