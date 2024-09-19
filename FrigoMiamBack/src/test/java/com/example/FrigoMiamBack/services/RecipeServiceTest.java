package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.DTO.IngredientQuantityDTO;
import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.factories.IngredientFactory;
import com.example.FrigoMiamBack.factories.RecipeFactory;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.TypeIngredient;
import com.example.FrigoMiamBack.utils.enums.Unit;
import org.aspectj.weaver.ast.Not;
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
public class RecipeServiceTest {
    @Autowired
    private RecipeRepository recipeRepository;
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        recipeService = new RecipeService(recipeRepository, accountRepository, ingredientRepository);
    }

    @Nested
    class FindRecipeTest{
        @Test
        public void ShouldReturnRecipe_WhenRecipeExists(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());
            Recipe createdRecipe = recipeRepository.save(recipe);

            Recipe foundRecipe = recipeService.findByID(createdRecipe.getId());

            assertEquals(foundRecipe, createdRecipe);
        }

        @Test
        public void ShouldReturnNull_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());

            Recipe foundRecipe = recipeService.findByID(UUID.randomUUID());

            assertNotEquals(foundRecipe, recipe);
        }
    }

    @Nested
    class FindAllRecipesTest{
        @Test
        public void ShouldReturnListOfRecipes_WhenRecipesExist() {
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Recipe createdRecipe = recipeRepository.save(recipe);
            Recipe recipe2 = RecipeFactory.createDefaultRecipe();
            Recipe createdRecipe2 = recipeRepository.save(recipe2);

            List<Recipe> expected = List.of(createdRecipe, createdRecipe2);
            List<Recipe> actual = recipeService.findAll();

            assertEquals(expected, actual);
        }

        @Test
        public void ShouldReturnEmptyList_WhenNoRecipesExist() {
            List<Recipe> actual = recipeService.findAll();

            assertEquals(0, actual.size());
        }
    }

    @Nested
    class AddRecipeTest{
        @Test
        public void ShouldThrowConflictException_WhenRecipeAlreadyExists(){
            Account account = AccountFactory.createDefaultAccount();
            Account createdAccount = accountRepository.save(account);

            UUID id = UUID.randomUUID();
            Recipe recipe = RecipeFactory.createRecipeWithId(id);
            recipeRepository.save(recipe);

            Recipe newRecipe = RecipeFactory.createRecipeWithId(id);

            List<IngredientQuantityDTO> ingredients = new ArrayList<>();
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            ingredients.add(new IngredientQuantityDTO(ingredient, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient, 5));

            ConflictException thrown = assertThrows(ConflictException.class, () -> recipeService.addRecipe(newRecipe, createdAccount, ingredients));

            assertEquals(ExceptionsMessages.RECIPE_ALREADY_EXIST, thrown.getMessage());
        }
        @Test
        public void ShouldReturnCreatedRecipe_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            List<IngredientQuantityDTO> ingredients = new ArrayList<>();
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            Ingredient ingredient2 = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            Ingredient ingredient3 = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            ingredients.add(new IngredientQuantityDTO(ingredient, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient2, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient3, 5));

            Recipe result = recipeService.addRecipe(recipe, account, ingredients);

            assertNotNull(result.getId());
            assertEquals(recipe.getTitle(), result.getTitle());
            assertEquals(recipe.getDescription(), result.getDescription());
            assertEquals(recipe.getInstructions(), result.getInstructions());
            assertEquals(recipe.getPreparation_time(), result.getPreparation_time());
            assertEquals(recipe.getCooking_time(), result.getCooking_time());
            assertEquals(recipe.getCalories(), result.getCalories());
            assertEquals(recipe.getTypeRecipe(), result.getTypeRecipe());
            assertEquals(recipe.getValidation(), result.getValidation());
            assertEquals(recipe.getDiet(), result.getDiet());
            assertEquals(recipe.getAccount(), result.getAccount());
            assertEquals(recipe.getRecipeIngredientsList(), result.getRecipeIngredientsList());
        }
    }

    @Nested
    class UpdateRecipeTest{
        @Test
        public void ShouldUpdateRecipe_WhenRecipeExists(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            recipeRepository.save(recipe);

            recipe.setDescription("new description");
            Recipe expected = recipeService.updateRecipe(recipe);

            assertEquals(recipe, expected);
        }
        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            recipe.setId(UUID.randomUUID());

            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.updateRecipe(recipe));

            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, thrown.getMessage());
        }
        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.updateRecipe(recipe));

            assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
        }
    }

    @Nested
    class DeleteRecipeTest{
        @Test
        public void ShouldReturnTrue_WhenRecipeExists(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Recipe savedRecipe = recipeRepository.save(recipe);

            boolean result = recipeService.deleteRecipe(savedRecipe.getId());

            assertTrue(result);
        }

        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());

            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(recipe.getId()));

            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.deleteRecipe(null));

            assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
        }
    }

    @Nested
    class GetRecipeTest{
        @Test
        public void ShouldHaveRecipe_WithRightDietParameter(){
            List<IngredientQuantityDTO> ingredientsDTO = new ArrayList<>();
            List<Ingredient> ingredients = new ArrayList<>();

            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            Ingredient beef = ingredientRepository.save(IngredientFactory.createIngredient("Boeuf hâché", Unit.GR, TypeIngredient.MEAT, null));
            Ingredient carrot = ingredientRepository.save(IngredientFactory.createIngredient("Carrot", Unit.GR, TypeIngredient.VEGETABLE, null));
//            ingredients.add(ingredient);
            ingredients.add(beef);
            ingredients.add(carrot);

            IngredientQuantityDTO ingredientDTO = new IngredientQuantityDTO(ingredient, 5);
            ingredientsDTO.add(ingredientDTO);
            IngredientQuantityDTO ingredientDTOBeef = new IngredientQuantityDTO(beef, 5);
            ingredientsDTO.add(ingredientDTOBeef);
            IngredientQuantityDTO ingredientDTOCarrot = new IngredientQuantityDTO(carrot, 5);
            ingredientsDTO.add(ingredientDTOCarrot);

            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            Recipe recipe = RecipeFactory.createDefaultRecipe();
            recipeService.addRecipe(recipe, account, ingredientsDTO);

            List<Recipe> found = recipeService.getRecipesByFilters(ingredients, null, Diet.VEGETARIAN);
//            System.out.println(found);
//            assertEquals(recipe, found.get(0));
        }
    }

    @Nested
    class GetFavoriteRecipes{
        @Test
        public void testGetFavoriteRecipesSuccess(){
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            account.getRecipeLikedList().add(recipe);
            accountRepository.save(account);

            List<Recipe> likedRecipes = recipeService.getFavoriteRecipes(account.getId());

            assertEquals(recipe, likedRecipes.get(0));
        }

        @Test
        public void testGetFavoriteRecipes_WithoutAccountId(){
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.getFavoriteRecipes(null));
            assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
        }

        @Test
        public void testGetFavoriteRecipes_WhenAccountDoesNotExist(){
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.getFavoriteRecipes(UUID.randomUUID()));
            assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
        }
    }

    @Nested
    class AddGradeToRecipe{
        @Test
        public void ShouldReturnTrue_WhenRecipeHasNoGrade(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            boolean result = recipeService.addGradeToRecipe(recipe, account, 3);

            assertTrue(result);
        }

        @Test
        public void ShouldReturnTrue_WhenAnotherAccountAddRecipeGrades(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            Account account2 = accountRepository.save(AccountFactory.createAccountWithEmail("test2@mail.fr"));
            recipeService.addGradeToRecipe(recipe, account, 3);
            boolean result = recipeService.addGradeToRecipe(recipe, account2, 5);

            assertTrue(result);
        }

        @Test
        public void ShouldThrowWrongParameterException_WhenGradeNegative(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addGradeToRecipe(recipe, account, -3));
            assertEquals(ExceptionsMessages.GRADE_CANNOT_BE_NEGATIVE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WhenGradeHigherThan_5(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addGradeToRecipe(recipe, account, 8));
            assertEquals(ExceptionsMessages.GRADE_CANNOT_BE_HIGHER_THAN_5, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addGradeToRecipe(recipe, account, 3));
            assertEquals(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.addGradeToRecipe(recipe, account, 3));
            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutAccountId(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = AccountFactory.createDefaultAccount();
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addGradeToRecipe(recipe, account, 3));
            assertEquals(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenAccountDoesNotExist(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.addGradeToRecipe(recipe, account, 3));
            assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowConflictException_WhenAlreadyGradedByAccount(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            recipeService.addGradeToRecipe(recipe, account, 3);
            ConflictException thrown = assertThrows(ConflictException.class, () -> recipeService.addGradeToRecipe(recipe, account, 3));
            assertEquals(ExceptionsMessages.ACCOUNT_ALREADY_GRADED_CANNOT_GRADE, thrown.getMessage());
        }
    }

    @Nested
    class GetAverageGradeTest{
        @Test void ShouldHaveCorrectAverageGrade_WhenGradesAreAdded(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            Account account2 = accountRepository.save(AccountFactory.createAccountWithEmail("test2@mail.fr"));

            recipeService.addGradeToRecipe(recipe, account, 3);
            recipeService.addGradeToRecipe(recipe, account2, 5);

            int average = recipeService.getAverageGrade(recipe.getId());

            assertEquals(4, average);
        }

        @Test void ShouldReturn_0_WhenRecipeHasNoGrade(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            int average = recipeService.getAverageGrade(recipe.getId());

            assertEquals(0, average);
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.getAverageGrade(recipe.getId()));
            assertEquals(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GET_AVERAGE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.getAverageGrade(recipe.getId()));
            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GET_AVERAGE, thrown.getMessage());
        }
    }

    @Nested
    class GetAccountGradeTest{
        @Test
        public void ShouldReturnAccountGrade_WhenRecipeHaveGrade(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            recipeService.addGradeToRecipe(recipe, account, 3);
            Integer grade = recipeService.getAccountGrade(recipe.getId(), account.getId());

            assertEquals(3, grade);
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.getAccountGrade(recipe.getId(), account.getId()));
            assertEquals(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GET_ACCOUNT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutAccountId(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = AccountFactory.createDefaultAccount();

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.getAccountGrade(recipe.getId(), account.getId()));
            assertEquals(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_GET_ACCOUNT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.getAccountGrade(UUID.randomUUID(), account.getId()));
            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GET_ACCOUNT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenAccountDoesNotExist(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.getAccountGrade(recipe.getId(), UUID.randomUUID()));
            assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_GET_ACCOUNT_GRADE, thrown.getMessage());
        }

        @Test
        public void ShouldReturnNull_WhenNoGradeByAccount(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());

            Integer grade = recipeService.getAccountGrade(recipe.getId(), account.getId());
            assertNull(grade);
        }
    }

    @Nested
    class AddIngredientToRecipeTest{
        @Test
        public void ShouldHaveAIngredientInRecipe_WhenIngredientAdded(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());

            Recipe result = recipeService.addIngredientToRecipe(recipe, ingredient, 10);
            assertEquals(result.getRecipeIngredientsList().get(0).getIngredient(), ingredient);
        }

        @Test
        public void ShouldThrowConflictException_WhenIngredientAlreadyAdded(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            recipeService.addIngredientToRecipe(recipe, ingredient, 10);
            ConflictException thrown = assertThrows(ConflictException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 10));
            assertEquals(ExceptionsMessages.INGREDIENT_ALREADY_IN_RECIPE, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithWrongQuantity(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            recipeService.addIngredientToRecipe(recipe, ingredient, 10);
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 0));
            assertEquals(ExceptionsMessages.QUANTITY_CANNOT_BE_ZERO_OR_LESS_CANNOT_ADD_INGREDIENT, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutRecipeId(){
            Recipe recipe = RecipeFactory.createDefaultRecipe();
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 10));
            assertEquals(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_ADD_INGREDIENT, thrown.getMessage());
        }

        @Test
        public void ShouldThrowWrongParameterException_WithoutIngredientId(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 10));
            assertEquals(ExceptionsMessages.EMPTY_INGREDIENT_ID_CANNOT_ADD_INGREDIENT, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist(){
            Recipe recipe = RecipeFactory.createRecipeWithId(UUID.randomUUID());
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 10));
            assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_ADD_INGREDIENT, thrown.getMessage());
        }

        @Test
        public void ShouldThrowNotFoundException_WhenIngredientDoesNotExist(){
            Recipe recipe = recipeRepository.save(RecipeFactory.createDefaultRecipe());
            Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(UUID.randomUUID());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> recipeService.addIngredientToRecipe(recipe, ingredient, 10));
            assertEquals(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST_CANNOT_ADD_INGREDIENT, thrown.getMessage());
        }
    }

    @Nested
    class GetRecipeCreatedTest{
        @Test
        public void ShouldHaveListOfCreatedRecipe(){
            Account account = accountRepository.save(AccountFactory.createDefaultAccount());
            Recipe recipe = RecipeFactory.createDefaultRecipe();

            List<IngredientQuantityDTO> ingredients = new ArrayList<>();
            Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            Ingredient ingredient2 = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            Ingredient ingredient3 = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            ingredients.add(new IngredientQuantityDTO(ingredient, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient2, 5));
            ingredients.add(new IngredientQuantityDTO(ingredient3, 5));

            Recipe savedRecipe = recipeService.addRecipe(recipe, account, ingredients);

            List<Recipe> createdRecipeList = recipeService.getRecipeCreated(account.getId());
            System.out.println("Account du Recipe" + " " + savedRecipe.getAccount());
            System.out.println("recipe du Account" + " " + account.getRecipeCreatedList().get(0).getTitle());
            System.out.println(createdRecipeList);

            assertEquals(1, createdRecipeList.size());
        }
    }

}
