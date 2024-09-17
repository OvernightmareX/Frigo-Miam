package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.IngredientFactory;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
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
public class IngredientServiceTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    private IngredientService ingredientService;

    @BeforeEach
    public void setup() {
        ingredientService = new IngredientService(ingredientRepository);
    }

    @Test
    public void testCreateIngredient_WhenIngredientDoesNotExist() {
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientService.addIngredient(ingredient);

        assertNotNull(savedIngredient.getId());
        assertEquals(ingredient.getId(), savedIngredient.getId());
        assertEquals(ingredient.getName(), savedIngredient.getName());
        assertEquals(ingredient.getUnit(), savedIngredient.getUnit());
        assertEquals(ingredient.getTypeIngredient(), savedIngredient.getTypeIngredient());
        assertEquals(ingredient.getAllergy(), savedIngredient.getAllergy());
    }

    @Test
    public void testCreateIngredient_WhenIngredientAlreadyExists() {
        UUID id = UUID.randomUUID();
        Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(id);
        ingredientRepository.save(ingredient);

        Ingredient newIngredient = IngredientFactory.createIngredientWithCustomId(id);

        ConflictException thrown = assertThrows(ConflictException.class, () -> ingredientService.addIngredient(newIngredient));

        assertEquals(ExceptionsMessages.INGREDIENT_ALREADY_EXIST, thrown.getMessage());
    }

    @Test
    public void testFindIngredient_WhenIngredientExists_ReturnIngredient() {
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient saved = this.ingredientService.addIngredient(ingredient);

        Ingredient expected = this.ingredientService.getIngredientById(saved.getId().toString());

        assertEquals(expected, saved);
    }

    @Test
    public void testFindIngredient_WhenIngredientDoesNotExist_ReturnNull() {
        Ingredient ingredient = this.ingredientService.getIngredientById(UUID.randomUUID().toString());
        assertNull(ingredient);
    }

    @Test
    public void testFindIngredient_WithoutIngredientId(){
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.ingredientService.getIngredientById(null));

        assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_FIND_INGREDIENT, thrown.getMessage());
    }

    @Test
    public void testFindAllIngredients_WhenAllIngredientsExist() {
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient ingredient2 = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientService.addIngredient(ingredient);
        Ingredient savedIngredient2 = this.ingredientService.addIngredient(ingredient2);

        List<Ingredient> expected = List.of(savedIngredient, savedIngredient2);
        List<Ingredient> actual = this.ingredientService.getAllIngredients();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllIngredients_WhenNoIngredientsExist_ReturnEmptyList() {
        List<Ingredient> expected = new ArrayList<>();
        List<Ingredient> actual = this.ingredientService.getAllIngredients();

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteIngredient_WhenIngredientExists(){
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientService.addIngredient(ingredient);

        boolean result = this.ingredientService.deleteIngredient(savedIngredient.getId().toString());

        assertTrue(result);
    }

    @Test
    public void testDeleteIngredient_WhenIngredientDoesNotExist(){
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.ingredientService.deleteIngredient(UUID.randomUUID().toString()));

        assertEquals(ExceptionsMessages.NO_INGREDIENT_FOUND_CANNOT_DELETE, thrown.getMessage());
    }

    @Test
    public void testDeleteIngredient_WithoutIngredientId(){
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.ingredientService.deleteIngredient(null));

        assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_DELETE_INGREDIENT, thrown.getMessage());
    }

    @Test
    public void testUpdateIngredient_WhenIngredientExists(){
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientService.addIngredient(ingredient);

        savedIngredient.setName("New name");
        Ingredient result = this.ingredientService.updateIngredient(savedIngredient);

        assertEquals(savedIngredient, result);
    }

    @Test
    public void testUpdateIngredient_WhenIngredientDoesNotExist(){
        Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(UUID.randomUUID());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.ingredientService.updateIngredient(ingredient));

        assertEquals(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testUpdateIngredient_WithoutIngredientId(){
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.ingredientService.updateIngredient(ingredient));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }
}
