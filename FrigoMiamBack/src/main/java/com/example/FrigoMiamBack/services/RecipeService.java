package com.example.FrigoMiamBack.services;


import com.example.FrigoMiamBack.DTO.IngredientQuantityDTO;
import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IRecipeService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;
import com.example.FrigoMiamBack.utils.enums.Role;
import com.example.FrigoMiamBack.utils.enums.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RecipeService implements IRecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final AccountRepository accountRepository;

    public RecipeService(RecipeRepository recipeRepository, AccountRepository accountRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Recipe findByID(UUID id) {
        if (id == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS_CANNOT_FIND_ACCOUNT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        return this.recipeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> allRecipes = this.recipeRepository.findAll();
        return allRecipes.stream().filter(recipe -> recipe.getValidation() == Validation.VALIDATED).toList();
    }



    @Override
    public Recipe addRecipe(Recipe recipe, Account account, List<IngredientQuantityDTO> ingredients) {
        if (recipe.getId() != null) {
            throw new ConflictException(ExceptionsMessages.RECIPE_ALREADY_EXIST, HttpStatus.CONFLICT, LocalDateTime.now());
        }



        recipe.setValidation(account.getRole().equals(Role.ADMIN) ? Validation.VALIDATED : Validation.PENDING);
        Recipe savedRecipe = this.recipeRepository.save(recipe);

        ingredients.forEach(ing -> {
            addIngredientToRecipe(savedRecipe, ing.getIngredient(), ing.getQuantity());
        });

        savedRecipe.setAccount(account);
        account.getRecipeCreatedList().add(savedRecipe);

        try {
            return this.recipeRepository.save(savedRecipe);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if (recipe.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_UPDATE_RECIPE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(recipe.getId())) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_UPDATE_RECIPE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            return this.recipeRepository.save(recipe);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteRecipe(UUID id) {
        System.out.println("service" + id);
        if (id == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ID_CANNOT_DELETE_RECIPE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(id)) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        try {
            this.recipeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Recipe> getFavoriteRecipes(UUID accountId) {
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ID_CANNOT_FIND_FAVORITE_RECIPE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        Account accountFound;
        if (this.accountRepository.findById(accountId).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_FIND_FAVORITE_RECIPE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return this.accountRepository.findById(accountId).get().getRecipeLikedList();
    }

    @Override
    public List<Recipe> getPendingRecipes() {
        List<Recipe> allRecipes = this.recipeRepository.findAll();
        return allRecipes.stream().filter(recipe -> recipe.getValidation() == Validation.PENDING).toList();
    }

    @Override
    public List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, Diet diets) {
        List<Recipe> allRecipes = this.recipeRepository.findAll();

        List<Recipe> filteredByDiet = new ArrayList<>();
        if (diets != null) {
            if(!EnumSet.allOf(Diet.class).contains(diets)) {
                throw new NotFoundException(ExceptionsMessages.DIET_DOES_NOT_EXIST_CANNOT_FILTER, HttpStatus.NOT_FOUND, LocalDateTime.now());
            }
            filteredByDiet = allRecipes.stream().filter(recipe -> recipe.getDiet() == diets).toList();
        } else {
            filteredByDiet = allRecipes;
        }

        List<Recipe> filteredByIngredients;
        if (ingredients != null) {
            filteredByIngredients = new ArrayList<>();
            for (Ingredient ing : ingredients) {
                if(!ingredientRepository.existsById(ing.getId())){
                    throw new NotFoundException(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST_CANNOT_FILTER, HttpStatus.NOT_FOUND, LocalDateTime.now());
                }
                for (Recipe recipe : filteredByDiet) {
                    List<Recipe_Ingredient> recipeAllIngredients = recipe.getRecipeIngredientsList();
                    recipeAllIngredients.forEach(ingr -> {
                        if (ingr.getIngredient().getId() == ing.getId()) {
                            filteredByIngredients.add(recipe);
                        }
                    });
                }
            }
        } else {
            filteredByIngredients = filteredByDiet;
        }

        List<Recipe> filteredByAllergens = new ArrayList<>();
        if (allergies != null) {
            for(Allergy all : allergies) {
                if(!EnumSet.allOf(Allergy.class).contains(all)) {
                    throw new NotFoundException(ExceptionsMessages.ALLERGY_DOES_NOT_EXIST_CANNOT_FILTER, HttpStatus.NOT_FOUND, LocalDateTime.now());
                }
            }

            for (Recipe recipe : filteredByIngredients) {
                List<Recipe_Ingredient> recipeIngredients = recipe.getRecipeIngredientsList();
                boolean hasAllergen = false;
                for (Recipe_Ingredient recipeIngredient : recipeIngredients) {
                    Ingredient ingredient = recipeIngredient.getIngredient();
                    Allergy ingrAllergen = ingredient.getAllergy();

                    if (allergies.contains(ingrAllergen)) {
                        hasAllergen = true;
                        break;
                    }
                }
                if (!hasAllergen) {
                    filteredByAllergens.add(recipe);
                }
            }
        } else {
            filteredByAllergens = filteredByIngredients;
        }

        return filteredByAllergens;
    }

    @Override
    public boolean addGradeToRecipe(Recipe recipe, Account account, int grade) {
        if (grade < 0) {
            throw new WrongParameterException(ExceptionsMessages.GRADE_CANNOT_BE_NEGATIVE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (grade > 5) {
            throw new WrongParameterException(ExceptionsMessages.GRADE_CANNOT_BE_HIGHER_THAN_5, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (recipe.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(recipe.getId())) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if (account.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (this.accountRepository.findById(account.getId()).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Grade_Recipe gradeRecipe = Grade_Recipe.builder()
                .account(account)
                .recipe(recipe)
                .rate(grade)
                .build();

        List<Grade_Recipe> recipeGrades = recipe.getRecipeGradesList();

        if (recipeGrades == null) {
            recipeGrades = new ArrayList<>();
            recipeGrades.add(gradeRecipe);
            recipe.setRecipeGradesList(recipeGrades);
        } else {
            recipeGrades.forEach(el -> {
                if (el.getAccount() == account) {
                    throw new ConflictException(ExceptionsMessages.ACCOUNT_ALREADY_GRADED_CANNOT_GRADE, HttpStatus.CONFLICT, LocalDateTime.now());
                }
            });
            recipe.getRecipeGradesList().add(gradeRecipe);
        }

        try {
            this.recipeRepository.save(recipe);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getAverageGrade(UUID recipeId) {
        if (recipeId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GET_AVERAGE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (this.recipeRepository.findById(recipeId).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GET_AVERAGE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        } else {
            Recipe recipe = this.recipeRepository.findById(recipeId).get();
            AtomicInteger total = new AtomicInteger();
            List<Grade_Recipe> grades = recipe.getRecipeGradesList();
            if (grades.isEmpty()) {
                return 0;
            } else {
                grades.forEach(grade -> {
                    total.addAndGet(grade.getRate());
                });
                return total.get() / grades.size();
            }
        }
    }

    @Override
    public Integer getAccountGrade(UUID recipeId, UUID accountId) {
        if (recipeId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GET_ACCOUNT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_GET_ACCOUNT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (this.accountRepository.findById(accountId).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_GET_ACCOUNT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if (this.recipeRepository.findById(recipeId).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GET_ACCOUNT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        List<Grade_Recipe> recipeGrades = this.recipeRepository.findById(recipeId).get().getRecipeGradesList();
        if (recipeGrades.stream().noneMatch(grade -> grade.getAccount().getId().equals(accountId))) {
            return null;
        } else {
            Grade_Recipe accountGrade = recipeGrades.stream().filter(gradeRecipe -> gradeRecipe.getAccount().getId().equals(accountId)).findFirst().get();
            return accountGrade.getRate();
        }
    }

    @Override
    public Recipe addIngredientToRecipe(Recipe recipe, Ingredient ingredient, double quantity) {
        if (quantity <= 0) {
            throw new WrongParameterException(ExceptionsMessages.QUANTITY_CANNOT_BE_ZERO_OR_LESS_CANNOT_ADD_INGREDIENT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (recipe.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_ADD_INGREDIENT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (ingredient.getId() == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_INGREDIENT_ID_CANNOT_ADD_INGREDIENT, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (!this.recipeRepository.existsById(recipe.getId())) {
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_ADD_INGREDIENT, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if (!this.ingredientRepository.existsById(ingredient.getId())) {
            throw new NotFoundException(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST_CANNOT_ADD_INGREDIENT, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Recipe_Ingredient addedIngredient = Recipe_Ingredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity(quantity)
                .build();

        List<Recipe_Ingredient> recipeIngredients = recipe.getRecipeIngredientsList();
        if (recipeIngredients.isEmpty()) {
            recipeIngredients.add(addedIngredient);
        } else {
            recipeIngredients.forEach(el -> {
                if (el.getIngredient() == ingredient) {
                    throw new ConflictException(ExceptionsMessages.INGREDIENT_ALREADY_IN_RECIPE, HttpStatus.CONFLICT, LocalDateTime.now());
                }
            });
            recipe.getRecipeIngredientsList().add(addedIngredient);
        }

        try {
            return recipe;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Recipe> getRecipeCreated(UUID accountId) {
        if (accountId == null) {
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_FIND_RECIPE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if (this.accountRepository.findById(accountId).isEmpty()) {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_FIND_RECIPES, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return this.accountRepository.findById(accountId).get().getRecipeCreatedList();
    }
}
