package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.interfaces.IIngredientService;
import com.example.FrigoMiamBack.utils.constants.ApiUrls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiUrls.INGREDIENT)
public class IngredientController {
    private final IIngredientService iIngredientService;

    public IngredientController(IIngredientService iIngredientService) {
        this.iIngredientService = iIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        System.out.println("IN IT !!! ");
        return new ResponseEntity<>(this.iIngredientService.getAllIngredients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable @NotBlank String id) {
        return new ResponseEntity<>(this.iIngredientService.getIngredientById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@Valid @RequestBody Ingredient ingredient) {
        return new ResponseEntity<>(this.iIngredientService.addIngredient(ingredient), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Ingredient> updateIngredient(@Valid @RequestBody Ingredient ingredient) {
        return new ResponseEntity<>(this.iIngredientService.updateIngredient(ingredient), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteIngredient(@Valid @RequestBody Ingredient ingredient) {
        return new ResponseEntity<>(this.iIngredientService.deleteIngredient(ingredient.getId()), HttpStatus.OK);
    }
}
