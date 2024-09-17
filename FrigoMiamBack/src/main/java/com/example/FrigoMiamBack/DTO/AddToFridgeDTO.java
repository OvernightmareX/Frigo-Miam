package com.example.FrigoMiamBack.DTO;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToFridgeDTO {
    @NotNull
    private Account account;
    @NotNull
    private Ingredient ingredient;
    @NotNull
    private int quantity;
}
