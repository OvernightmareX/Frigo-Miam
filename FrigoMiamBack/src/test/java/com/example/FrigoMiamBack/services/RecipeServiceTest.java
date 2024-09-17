package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.factories.RecipeFactory;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Diet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeServiceTest {
    @Autowired
    private RecipeRepository recipeRepository;
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(accountRepository, recipeRepository, ingredientRepository);
        recipeService = new RecipeService(recipeRepository, accountService);
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

    @Test
    public void testFindAllRecipes_WhenRecipesExist() {
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        Recipe createdRecipe = recipeRepository.save(recipe);
        Recipe recipe2 = RecipeFactory.createDefaultRecipe();
        Recipe createdRecipe2 = recipeRepository.save(recipe2);

        List<Recipe> expected = List.of(createdRecipe, createdRecipe2);
        List<Recipe> actual = this.recipeService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllRecipes_WhenNoRecipesExist() {
        List<Recipe> expected = new ArrayList<>();
        List<Recipe> actual = this.recipeService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddRecipe_WhenRecipeExists(){
        UUID id = UUID.randomUUID();
        Recipe recipe = RecipeFactory.createRecipeWithId(id);
        recipeRepository.save(recipe);

        Recipe newRecipe = RecipeFactory.createRecipeWithId(id);

        ConflictException thrown = assertThrows(ConflictException.class, () -> recipeService.addRecipe(newRecipe));

        assertEquals(ExceptionsMessages.RECIPE_ALREADY_EXIST, thrown.getMessage());
    }

    @Test
    public void testAddRecipe_WhenRecipeDoesNotExist(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        Recipe result = this.recipeService.addRecipe(recipe);

        assertNotNull(result.getId_recipe());
        assertEquals(recipe.getTitle(), result.getTitle());
        assertEquals(recipe.getDescription(), result.getDescription());
        assertEquals(recipe.getInstructions(), result.getInstructions());
        assertEquals(recipe.getPreparation_time(), result.getPreparation_time());
        assertEquals(recipe.getCooking_time(), result.getCooking_time());
        assertEquals(recipe.getCalories(), result.getCalories());
        assertEquals(recipe.getTypeRecipe(), result.getTypeRecipe());
        assertEquals(recipe.getValidation(), result.getValidation());
        assertEquals(recipe.getDiet(), result.getDiet());
    }

    @Test
    public void testUpdateRecipe_WhenRecipeExists(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        this.recipeService.addRecipe(recipe);

        recipe.setDescription("new description");
        Recipe expected = this.recipeService.updateRecipe(recipe);

        assertEquals(recipe, expected);
    }

    @Test
    public void testUpdateRecipe_WhenRecipeDoesNotExist(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        recipe.setId_recipe(UUID.randomUUID());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.recipeService.updateRecipe(recipe));

        assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testUpdateRecipe_WithoutRecipeId(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.recipeService.updateRecipe(recipe));

        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testDeleteRecipe_WhenRecipeExists(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        Recipe savedRecipe = this.recipeService.addRecipe(recipe);

        boolean result = this.recipeService.deleteRecipe(savedRecipe.getId_recipe().toString());

        assertTrue(result);
    }

    @Test
    public void testDeleteRecipe_WhenRecipeDoesNotExist(){
        Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.recipeService.deleteRecipe(recipe.getId_recipe().toString()));

        assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testDeleteRecipe_WithoutRecipeId(){

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.recipeService.deleteRecipe(null));

        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testGetRecipeByDiet(){
        Recipe recipe = RecipeFactory.createDefaultRecipe();
        this.recipeService.addRecipe(recipe);

        List<Recipe> found = this.recipeService.getRecipesByFilters(null, null, Diet.VEGETARIAN);

        assertEquals(recipe, found.get(0));
    }

    @Test
    public void testGetFavoriteRecipesSuccess(){
        Account account = accountRepository.save(AccountFactory.createDefaultAccount());
        Recipe recipe = this.recipeRepository.save(RecipeFactory.createDefaultRecipe());
        account.getRecipeLikedList().add(recipe);
        accountRepository.save(account);

        List<Recipe> likedRecipes = this.recipeService.getFavoriteRecipes(account.getId().toString());
        assertEquals(recipe, likedRecipes.get(0));
    }
}
