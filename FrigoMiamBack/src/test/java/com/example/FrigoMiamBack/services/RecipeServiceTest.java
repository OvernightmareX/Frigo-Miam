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

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeServiceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    public void testFindRecipe_WhenRecipeExists(){

    }
}
