package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.DTO.AccountGradeDTO;
import com.example.FrigoMiamBack.DTO.AddRecipeDTO;
import com.example.FrigoMiamBack.DTO.AddRecipeGradeDTO;
import com.example.FrigoMiamBack.DTO.RecipeFilterDTO;
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
    public ResponseEntity<Recipe> getRecipeById(@PathVariable @NotBlank String recipeId) {
        return new ResponseEntity<>(this.iRecipeService.findByID(UUID.fromString(recipeId)), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.FAVORITE)
    public ResponseEntity<List<Recipe>> getFavoriteRecipes(@PathVariable @NotBlank String accountId) {
        return new ResponseEntity<>(this.iRecipeService.getFavoriteRecipes(UUID.fromString(accountId)), HttpStatus.OK);
    }

    @PostMapping(ApiUrls.FILTER)
    public ResponseEntity<List<Recipe>> getRecipesByFilters(@Valid @RequestBody RecipeFilterDTO recipeFilterDTO) {
        return new ResponseEntity<>(this.iRecipeService.getRecipesByFilters(recipeFilterDTO.getIngredientList(), recipeFilterDTO.getAllergyList(), recipeFilterDTO.getDiet()), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.ACCOUNT)
    public ResponseEntity<Integer> getAccountGrade(@Valid @RequestBody AccountGradeDTO accountGradeDTO) {
        return new ResponseEntity<>(this.iRecipeService.getAccountGrade(accountGradeDTO.getRecipeID(), accountGradeDTO.getAccountID()), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.ACCOUNT + ApiUrls.CREATED)
    public ResponseEntity<List<Recipe>> getRecipeCreated(@PathVariable @NotBlank String accountId) {
        return new ResponseEntity<>(this.iRecipeService.getRecipeCreated(UUID.fromString(accountId)), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.AVERAGE)
    public ResponseEntity<Integer> getAverageGrade(@PathVariable @NotBlank String recipeId) {
        return new ResponseEntity<>(this.iRecipeService.getAverageGrade(UUID.fromString(recipeId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody AddRecipeDTO recipeDTO) {
        return new ResponseEntity<>(this.iRecipeService.addRecipe(recipeDTO.getRecipe(), recipeDTO.getAccount(), recipeDTO.getIngredients()), HttpStatus.CREATED);
    }

    @PostMapping(ApiUrls.GRADE)
    public ResponseEntity<Boolean> addGradeToRecipe(@Valid @RequestBody AddRecipeGradeDTO recipeGradeDTO) {
        return new ResponseEntity<>(this.iRecipeService.addGradeToRecipe(recipeGradeDTO.getRecipe(), recipeGradeDTO.getAccount(), recipeGradeDTO.getGrade()), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Recipe> updateRecipe(@Valid @RequestBody Recipe recipe) {
        return new ResponseEntity<>(this.iRecipeService.updateRecipe(recipe), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteRecipe(@Valid @RequestBody Recipe recipe) {
        return new ResponseEntity<>(this.iRecipeService.deleteRecipe(recipe.getId()), HttpStatus.OK);
    }
}
