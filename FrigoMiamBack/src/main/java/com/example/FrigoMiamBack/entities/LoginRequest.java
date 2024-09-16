package com.example.FrigoMiamBack.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "email cannot be empty !")
    private String email;
    @NotBlank(message = "password cannot be empty !")
    private String password;
}
