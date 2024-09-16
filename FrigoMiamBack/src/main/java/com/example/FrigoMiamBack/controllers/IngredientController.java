package com.example.FrigoMiamBack.controllers;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.interfaces.IAccountService;
import com.example.FrigoMiamBack.interfaces.IIngredientService;
import com.example.FrigoMiamBack.utils.constants.ApiUrls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.INGREDIENT)
public class IngredientController {
    private final IIngredientService iIngredientService;

    public IngredientController(IIngredientService iIngredientService) {
        this.iIngredientService = iIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return new ResponseEntity<>(this.iIngredientService.getAllIngredients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable @NotBlank String id) {
        return new ResponseEntity<>(this.iIngredientService.getIngredientById(id), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.FRIDGE)
    public ResponseEntity<List<Ingredient>> getFridge(@Valid @RequestBody Account account) {
        //TODO A tester !!!
        return new ResponseEntity<>(this.iIngredientService.getFridge(account.getId().toString()), HttpStatus.CREATED);
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
        return new ResponseEntity<>(this.iIngredientService.deleteIngredient(ingredient), HttpStatus.OK);
    }
}
