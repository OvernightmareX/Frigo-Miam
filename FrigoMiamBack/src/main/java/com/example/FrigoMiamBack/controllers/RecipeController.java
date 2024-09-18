package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.interfaces.IRecipeService;
import com.example.FrigoMiamBack.utils.constants.ApiUrls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiUrls.RECIPE)
public class RecipeController {
    private final IRecipeService iRecipeService;

    public RecipeController(IRecipeService iRecipeService) {
        this.iRecipeService = iRecipeService;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipe() {
        return new ResponseEntity<>(this.iRecipeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable @NotBlank String id) {
        return new ResponseEntity<>(this.iRecipeService.findByID(UUID.fromString(id)), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody Recipe recipe) {
//        return new ResponseEntity<>(this.iRecipeService.addRecipe(recipe), HttpStatus.CREATED);
//    }

    @PutMapping
    public ResponseEntity<Recipe> updateRecipe(@Valid @RequestBody Recipe recipe) {
        return new ResponseEntity<>(this.iRecipeService.updateRecipe(recipe), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteRecipe(@Valid @RequestBody Recipe recipe) {
        return new ResponseEntity<>(this.iRecipeService.deleteRecipe(recipe.getId()), HttpStatus.OK);
    }
}
