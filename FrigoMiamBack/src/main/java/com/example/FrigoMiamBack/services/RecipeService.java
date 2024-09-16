package com.example.FrigoMiamBack.services;

/*
@Service
public class RecipeService implements IRecipeService {
    private RecipeRepository recipeRepository;

    @Override
    public List<Recipe> getRecipesByFilters(List<Ingredient> ingredients, List<Allergy> allergies, List<Diet> diets) {
        return this.recipeRepository.findRecipesByIngredientAndAllergyAndDiet(ingredients, allergies, diets);
    }

    @Override
    public Recipe getRecipe(String id) {
        UUID recipeId = UUID.fromString(id);
        return this.recipeRepository.findById(recipeId).orElse(null);
    }

    @Override
    public int getAverageGrade(String recipeId) {
        UUID id = UUID.fromString(recipeId);
        try {
            List<Integer> grades = this.recipeRepository.findRecipeGrades(id);
            int count = 0;
            for(int grade : grades) {
                count += grade;
            }
            return count / grades.size();
        } catch (Exception e) {
            //TODO exception personnalisée
            return 0;
        }
    }

    @Override
    public int getAccountGrade(String recipeId, String accountId) {
        try {
            return this.recipeRepository.findGradeByRecipeAndAccount(UUID.fromString(recipeId), UUID.fromString(accountId));
        } catch (Exception e) {
            //TODO exception
            return 0;
        }
    }

    @Override
    public boolean addGradeToRecipe(String recipeId, String accountId, int grade) {
        return this.recipeRepository.addGradeToRecipe(UUID.fromString(recipeId), UUID.fromString(accountId), grade);
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        try {
            this.recipeRepository.save(recipe);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateRecipe(Recipe recipe) {
        try {
            this.recipeRepository.save(recipe);
            return true;
        } catch (Exception e) {
            //TODO exception
            return false;
        }
    }

    @Override
    public boolean deleteRecipe(String id) {
        try {
            this.recipeRepository.deleteById(UUID.fromString(id));
            return true;
        } catch (Exception e) {
            //TODO exception
            return false;
        }
    }

    @Override
    public List<Recipe> getFavoriteRecipes(String accountId) {
        try {
            return this.recipeRepository.findRecipeLikedList(UUID.fromString(accountId));
        } catch (Exception e) {
            //TODO exception
            return null;
        }
    }

    @Override
    public List<Recipe> getRecipeCreated(String accountId) {
        return this.recipeRepository.findrecipeCreatedList(UUID.fromString(accountId));
    }
}*/
