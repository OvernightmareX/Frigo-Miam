package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.factories.RecipeFactory;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeServiceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    public void testFindRecipe_WhenRecipeExists(){
        Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());
        Recipe createdRecipe = recipeRepository.save(recipe);

        Recipe foundRecipe = recipeService.findByID(createdRecipe.getId_recipe().toString());

        assertEquals(foundRecipe, createdRecipe);
    }

    @Test
    public void testFindRecipe_WhenRecipeDoesNotExist(){
        Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());

        Recipe foundRecipe = recipeService.findByID(UUID.randomUUID().toString());

        assertNotEquals(foundRecipe, recipe);
    }


}
