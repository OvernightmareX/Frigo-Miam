package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.factories.IngredientFactory;
import com.example.FrigoMiamBack.repositories.*;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
public class IngredientServiceTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private AccountRepository accountRepository;

    private IngredientService ingredientService;

    @BeforeEach
    public void setup() {
        ingredientService = new IngredientService(ingredientRepository);
    }

    @Nested
    class CreateIngredientTest{
        @Test
        public void ShouldHaveCreatedIngredient_WhenCreateIngredient_AndDoesNotExist() {
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientService.addIngredient(ingredient);

            assertNotNull(savedIngredient.getId());
            assertEquals(ingredient.getId(), savedIngredient.getId());
            assertEquals(ingredient.getName(), savedIngredient.getName());
            assertEquals(ingredient.getUnit(), savedIngredient.getUnit());
            assertEquals(ingredient.getTypeIngredient(), savedIngredient.getTypeIngredient());
            assertEquals(ingredient.getAllergy(), savedIngredient.getAllergy());
        }

        @Test
        public void ShouldThrowConflictException_WhenIngredientAlreadyExists() {
            UUID id = UUID.randomUUID();
            Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(id);
            ingredientRepository.save(ingredient);

            Ingredient newIngredient = IngredientFactory.createIngredientWithCustomId(id);

            ConflictException thrown = assertThrows(ConflictException.class, () -> ingredientService.addIngredient(newIngredient));

            assertEquals(ExceptionsMessages.INGREDIENT_ALREADY_EXIST, thrown.getMessage());
        }
    }

    @Nested
    class FindIngredientTest{
        @Test
        public void ShouldReturnIngredient_WhenIngredientExists() {
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient saved = ingredientService.addIngredient(ingredient);

            Ingredient expected = ingredientService.getIngredientById(saved.getId());

            assertEquals(expected, saved);
        }
        @Test
        public void ShouldReturnNull_WhenIngredientDoesNotExist() {
            Ingredient ingredient = ingredientService.getIngredientById(UUID.randomUUID());
            assertNull(ingredient);
        }
        @Test
        public void ShouldThrowWrongParameterException_WhenIngredientIdNull(){
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> ingredientService.getIngredientById(null));

            assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_FIND_INGREDIENT, thrown.getMessage());
        }
    }

    @Nested
    class FindAllIngredients{
        @Test
        public void ShouldHaveListOfAllIngredients_WhenAllIngredientsExist() {
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient ingredient2 = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientService.addIngredient(ingredient);
            Ingredient savedIngredient2 = ingredientService.addIngredient(ingredient2);

            List<Ingredient> expected = List.of(savedIngredient, savedIngredient2);
            List<Ingredient> actual = ingredientService.getAllIngredients();

            assertEquals(expected, actual);
        }
        @Test
        public void ShouldReturnEmptyList_WhenNoIngredientsExist() {
            List<Ingredient> expected = new ArrayList<>();
            List<Ingredient> actual = ingredientService.getAllIngredients();

            assertEquals(expected, actual);
        }
    }

    @Nested
    class DeleteIngredientTest{
        @Test
        public void ShouldReturnTrue_WhenIngredientExists(){
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientService.addIngredient(ingredient);

            boolean result = ingredientService.deleteIngredient(savedIngredient.getId());

            assertTrue(result);
        }

        @Test
        public void ShouldThrowNotFoundException_WhenIngredientDoesNotExist(){
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> ingredientService.deleteIngredient(UUID.randomUUID()));

            assertEquals(ExceptionsMessages.NO_INGREDIENT_FOUND_CANNOT_DELETE, thrown.getMessage());
        }

        @Test
        public void ShouldReturnWrongParameterException_WithoutIngredientId(){
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> ingredientService.deleteIngredient(null));

            assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_DELETE_INGREDIENT, thrown.getMessage());
        }
    }

    @Nested
    class UpdateIngredientTest{
        @Test
        public void ShouldUpdateIngredient_WhenIngredientExists(){
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientService.addIngredient(ingredient);

            savedIngredient.setName("New name");
            Ingredient result = ingredientService.updateIngredient(savedIngredient);

            assertEquals(savedIngredient, result);
        }

        @Test
        public void ShouldThrowNotFoundException_WhenIngredientDoesNotExist(){
            Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(UUID.randomUUID());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> ingredientService.updateIngredient(ingredient));

            assertEquals(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutIngredientId(){
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> ingredientService.updateIngredient(ingredient));
            assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_UPDATE_INGREDIENT, thrown.getMessage());
        }
    }

}
