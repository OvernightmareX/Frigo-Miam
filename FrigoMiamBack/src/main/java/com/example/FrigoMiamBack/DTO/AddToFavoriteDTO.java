package com.example.FrigoMiamBack.DTO;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToFavoriteDTO {
    @NotNull
    private Account account;
    @NotNull
    private Recipe recipe;
}
