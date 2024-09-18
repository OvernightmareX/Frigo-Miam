package com.example.FrigoMiamBack.utils.constants;

public class ExceptionsMessages {
    public static final String ACCOUNT_ALREADY_CREATED = "createAccount:: Account already saved trying to be created.";
    public static final String EMAIL_ALREADY_EXIST = "createAccount:: Account with email already exists.";

    public static final String ACCOUNT_NULL_CANNOT_CREATE_TOKEN = "generateToken:: Account is null.";
    public static final String ROLE_NULL_CANNOT_CREATE_TOKEN = "generateToken:: Role is null.";
    public static final String ACCOUNT_NULL_CANNOT_VALIDATE_TOKEN = "validateToken:: Account is null.";
    public static final String TOKEN_NULL_CANNOT_VALIDATE_TOKEN = "validateToken:: Token is null.";

    // ACCOUNT EXCEPTIONS
    public static final String EMPTY_ID_CANNOT_FIND_ACCOUNT = "findAccount:: Account id is empty.";
    public static final String EMPTY_ID_CANNOT_UPDATE_ACCOUNT = "updateAccount:: Account id is empty.";
    public static final String NO_ACCOUNT_FOUND_CANNOT_UPDATE = "updateAccount:: Account not found.";
    public static final String EMPTY_ID_CANNOT_DELETE_ACCOUNT = "deleteAccount:: Account id is empty.";
    public static final String NO_ACCOUNT_FOUND_CANNOT_DELETE = "deleteAccount:: Account not found.";

    public static final String EMPTY_ACCOUNT_ID_CANNOT_ADD_RECIPE_TO_FAVORITE = "addRecipeToFavorite:: Account id is empty.";
    public static final String NO_ACCOUNT_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE = "addRecipeToFavorite:: Account not found.";
    public static final String NO_RECIPE_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE = "addRecipeToFavorite:: Recipe not found.";
    public static final String EMPTY_RECIPE_ID_CANNOT_ADD_RECIPE_TO_FAVORITE = "addRecipeToFavorite:: Recipe id is empty.";

    public static final String QUANTITY_CANNOT_BE_ZERO_OR_LESS = "addIngredientToFridge:: Quantity cannot be zero or less.";
    public static final String EMPTY_ACCOUNT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE = "addIngredientToFridge:: Account id is empty.";
    public static final String NO_ACCOUNT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE = "addIngredientToFridge:: Account not found.";
    public static final String EMPTY_INGREDIENT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE = "addIngredientToFridge:: Ingredient id is empty.";
    public static final String NO_INGREDIENT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE = "addIngredientToFridge:: Ingredient not found.";
    public static final String INGREDIENT_ALREADY_ADDED_TO_FRIDGE = "addIngredientToFridge:: Ingredient already added.";

    public static final String EMPTY_ACCOUNT_ID_CANNOT_FIND_FRIDGE = "findFridge:: Account id is empty.";
    public static final String NO_ACCOUNT_FOUND_CANNOT_FIND_FRIDGE = "findFridge:: Account not found.";
    // ACCOUNT MESSAGES EXCEPTION OK

    // INGREDIENTS EXCEPTIONS
    public static final String EMPTY_ID_CANNOT_FIND_INGREDIENT = "findIngredient:: Ingredient id is empty.";
    public static final String EMPTY_ID_CANNOT_DELETE_INGREDIENT = "deleteIngredient:: Ingredient id is empty.";
    public static final String NO_INGREDIENT_FOUND_CANNOT_DELETE = "deleteIngredient:: Ingredient not found.";
    // CONTINUER A UPDATE INGREDIENT


    public static final String ACCOUNT_DOES_NOT_EXIST = "updateAccount:: No account found.";
    public static final String ACCOUNT_TO_LOGIN_DOES_NOT_EXIST = "login:: No account found.";
    public static final String WRONG_PARAMETERS = "update:: Wrong parameters.";

    public static final String RECIPE_ALREADY_EXIST = "createRecipe:: Recipe already exists.";
    public static final String RECIPE_DOES_NOT_EXIST = "updateRecipe:: No recipe found.";

    public static final String INGREDIENT_ALREADY_EXIST = "createIngredient:: Ingredient already exists.";
    public static final String INGREDIENT_DOES_NOT_EXIST = "updateIngredient:: No ingredient found.";

}
