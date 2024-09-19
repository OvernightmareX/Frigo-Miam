package com.example.FrigoMiamBack.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountGradeDTO {
    private UUID recipeID;
    private UUID accountID;
}
