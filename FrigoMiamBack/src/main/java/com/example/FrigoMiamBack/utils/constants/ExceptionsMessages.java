package com.example.FrigoMiamBack.utils.constants;

public class ExceptionsMessages {
    public static final String ACCOUNT_ALREADY_CREATED = "createAccount:: Account already saved trying to be created.";
    public static final String EMAIL_ALREADY_EXIST = "createAccount:: Account with email already exists.";
    public static final String ACCOUNT_NULL_CANNOT_CREATE_TOKEN = "generateToken:: Account is null.";
    public static final String ROLE_NULL_CANNOT_CREATE_TOKEN = "generateToken:: Role is null.";
    public static final String ACCOUNT_NULL_CANNOT_VALIDATE_TOKEN = "validateToken:: Account is null.";
    public static final String TOKEN_NULL_CANNOT_VALIDATE_TOKEN = "validateToken:: Token is null.";
    public static final String ACCOUNT_DOES_NOT_EXIST = "updateAccount:: No account found.";
    public static final String WRONG_PARAMETERS = "update:: Wrong parameters.";

    public static final String RECIPE_ALREADY_EXIST = "createRecipe:: Recipe already exists.";
    public static final String RECIPE_DOES_NOT_EXIST = "updateRecipe:: No recipe found.";

    public static final String INGREDIENT_ALREADY_EXIST = "createIngredient:: Ingredient already exists.";
    public static final String INGREDIENT_DOES_NOT_EXIST = "updateIngredient:: No ingredient found.";

    public static final String INGREDIENT_ALREADY_ADDED = "addIngredientToFridge:: Ingredient already added.";
}
