package com.example.FrigoMiamBack.services;


import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Grade_Recipe;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.interfaces.IRecipeService;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.Diet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class RecipeService implements IRecipeService {
    private final RecipeRepository recipeRepository;
    private final AccountRepository accountRepository;

    public RecipeService(RecipeRepository recipeRepository, AccountRepository accountRepository) {
        this.recipeRepository = recipeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Recipe findByID(UUID id) {
        if(id == null) {
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        return this.recipeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Recipe> findAll() {
        return this.recipeRepository.findAll();
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        if(recipe.getId() != null){
            throw new ConflictException(ExceptionsMessages.RECIPE_ALREADY_EXIST, HttpStatus.CONFLICT, LocalDateTime.now());
        }

        try {
            return this.recipeRepository.save(recipe);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if(recipe.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!this.recipeRepository.existsById(recipe.getId())){
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
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
        if(id == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!this.recipeRepository.existsById(id)){
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
        if(accountId == null){
            throw new WrongParameterException(ExceptionsMessages.WRONG_PARAMETERS, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        Account accountFound;
        if(this.accountRepository.findById(accountId).isPresent()){
            accountFound = this.accountRepository.findById(accountId).get();
        } else {
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        return accountFound.getRecipeLikedList();
    }

    @Override
    public List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, Diet diets) {
        List<Recipe> finalRecipes = this.recipeRepository.findAll();

        finalRecipes.forEach(recipe -> {

        });
        if(diets != null){
            finalRecipes = finalRecipes.stream().filter(recipe -> recipe.getDiet() == diets).toList();
        }
        //TODO AJOUTER UN INGREDIENT A UNE RECETTE POUR POUVOIR FILTRER/INGREDIENt et /ALLERGEN
//        if(ingredients != null){
//            for(Ingredient ingredient : ingredients){
//                for(Recipe recipe : finalRecipes){
//                    List<Recipe_Ingredient> recipeIngredients = recipe.getRecipeIngredientsList();
//                    System.out.println(recipeIngredients);
//                }
//                finalRecipes.stream().filter(recipe -> recipe.getRecipeIngredientsList())
//            }
//        }
        return finalRecipes;
    }

    @Override
    public boolean addGradeToRecipe(Recipe recipe, Account account, int grade) {
        if(grade < 0){
            throw new WrongParameterException(ExceptionsMessages.GRADE_CANNOT_BE_NEGATIVE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(grade > 5){
            throw new WrongParameterException(ExceptionsMessages.GRADE_CANNOT_BE_HIGHER_THAN_5, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(recipe.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(!this.recipeRepository.existsById(recipe.getId())){
            throw new NotFoundException(ExceptionsMessages.RECIPE_DOES_NOT_EXIST_CANNOT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        if(account.getId() == null){
            throw new WrongParameterException(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_GRADE, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }
        if(this.accountRepository.findById(account.getId()).isEmpty()){
            throw new NotFoundException(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST_CANNOT_GRADE, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Grade_Recipe gradeRecipe = Grade_Recipe.builder()
                .account(account)
                .recipe(recipe)
                .rate(grade)
                .build();

        List<Grade_Recipe> recipeGrades = recipe.getRecipeGradesList();

        if(recipeGrades == null){
            recipeGrades = new ArrayList<>();
            recipeGrades.add(gradeRecipe);
            recipe.setRecipeGradesList(recipeGrades);
        } else {
            recipeGrades.forEach(el -> {
                if(el.getAccount() == account){
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
    public int getAverageGrade(String recipeId) {
        Recipe recipe = this.recipeRepository.findById(UUID.fromString(recipeId)).get();
        AtomicInteger total = new AtomicInteger();
        List <Grade_Recipe> grades = recipe.getRecipeGradesList();
        grades.forEach(grade -> {
            total.addAndGet(grade.getRate());
        });
        return total.get() /grades.size();
    }

    @Override
    public int getAccountGrade(String recipeId, String accountId) {
        return 0;
    }

//
//    @Override
//    public int getAverageGrade(String recipeId) {
//        UUID id = UUID.fromString(recipeId);
//        try {
//            List<Integer> grades = this.recipeRepository.findRecipeGrades(id);
//            int count = 0;
//            for(int grade : grades) {
//                count += grade;
//            }
//            return count / grades.size();
//        } catch (Exception e) {
//            //TODO exception personnalisée
//            return 0;
//        }
//    }
//

//    @Override
//    public List<Recipe> getFavoriteRecipes(String accountId) {
//        try {
//            return this.recipeRepository.findRecipeLikedListByAccountId(UUID.fromString(accountId));
//        } catch (Exception e) {
//            //TODO exception
//            return null;
//        }
//    }
//
//    @Override
//    public List<Recipe> getRecipeCreated(String accountId) {
//        return this.recipeRepository.findrecipeCreatedList(UUID.fromString(accountId));
//    }
}
