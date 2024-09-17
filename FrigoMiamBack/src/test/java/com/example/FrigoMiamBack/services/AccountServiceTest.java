package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.factories.IngredientFactory;
import com.example.FrigoMiamBack.factories.RecipeFactory;
import com.example.FrigoMiamBack.repositories.AccountRepository;
import com.example.FrigoMiamBack.repositories.FridgeRepository;
import com.example.FrigoMiamBack.repositories.IngredientRepository;
import com.example.FrigoMiamBack.repositories.RecipeRepository;
import com.example.FrigoMiamBack.utils.constants.ExceptionsMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private AccountService accountService;

    private IngredientService ingredientService;

    @BeforeEach
    public void setup() {
        accountService = new AccountService(accountRepository, fridgeRepository, recipeRepository, ingredientService, ingredientRepository);
    }

    @Test
    public void testCheckEmail_WhenEmailExists() {
        String emailSearched = "email@test.com";
        Account account = AccountFactory.createAccountWithEmail(emailSearched);
        accountRepository.save(account);

        assertTrue(accountService.checkEmail(emailSearched));
    }

    @Test
    public void testCheckEmail_WhenEmailNotExists() {
        assertFalse(accountService.checkEmail("emailNotExist@test.fr"));
    }

    @Test
    public void testCreateAccountSuccess() {
        Account account = AccountFactory.createDefaultAccount();
        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount.getId());
        assertEquals(account.getFirstname(), createdAccount.getFirstname());
        assertEquals(account.getLastname(), createdAccount.getLastname());
        assertEquals(account.getPassword(), createdAccount.getPassword());
        assertEquals(account.getEmail(), createdAccount.getEmail());
        assertEquals(account.getBirthdate(), createdAccount.getBirthdate());
        assertEquals(account.getAllergies(), createdAccount.getAllergies());
        assertEquals(account.getDiets(), createdAccount.getDiets());
    }

    @Test
    public void testCreateAccountWithExistingEmail() {
        String emailTest = "already@exist.com";
        Account existingAccount = AccountFactory.createAccountWithEmail(emailTest);
        accountRepository.save(existingAccount);

        Account newAccount = AccountFactory.createAccountWithEmail(emailTest);
        ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(newAccount));

        assertEquals(ExceptionsMessages.EMAIL_ALREADY_EXIST, thrown.getMessage());
    }

    @Test
    public void testCreateAccountWithId() {
        UUID accountId = UUID.randomUUID();
        Account accountWithId = AccountFactory.createAccountWithId(accountId);

        ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(accountWithId));

        assertEquals(ExceptionsMessages.ACCOUNT_ALREADY_CREATED, thrown.getMessage());
    }

    @Test
    public void testGetAccountById_WithoutAccountId(){
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> {accountService.getAccountById(null);});
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testGetAccountById_WithAccountId(){
        Account account = AccountFactory.createDefaultAccount();
        Account saved = accountService.createAccount(account);

        Account found = accountService.getAccountById(saved.getId().toString());

        assertEquals(saved, found);
    }

    @Test
    public void testUpdateAccountSuccess() {
        Account account = AccountFactory.createDefaultAccount();
        accountService.createAccount(account);
        account.setFirstname("Totoro");
        Account updated = accountService.updateAccount(account);

        assertEquals(account.getFirstname(), updated.getFirstname());
    }

    @Test
    public void testUpdateAccount_WithoutAccountId(){
        Account account = AccountFactory.createDefaultAccount();
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testUpdateAccount_WhenAccountDoesNotExist(){
        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testDeleteAccountSuccess() {
        Account account = AccountFactory.createDefaultAccount();
        accountService.createAccount(account);
        boolean result = accountService.deleteAccount(account);
        assertTrue(result);
    }

    @Test
    public void testDeleteAccount_WithoutAccountId(){
        Account account = AccountFactory.createDefaultAccount();
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.deleteAccount(account));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testDeleteAccount_WhenAccountDoesNotExist(){
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.deleteAccount(AccountFactory.createAccountWithId(UUID.randomUUID())));
        assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testAddRecipeToFavoriteSuccess() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account createdAccount = this.accountService.createAccount(accountDefault);

        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();
        Recipe createdRecipe = this.recipeRepository.save(recipeDefault);

        Account accountUpdated = this.accountService.addRecipeToFavorite(createdAccount, createdRecipe);

        assertEquals(1, accountUpdated.getRecipeLikedList().size());
        assertEquals(createdRecipe, accountUpdated.getRecipeLikedList().get(0));
    }

    @Test
    public void testAddRecipeToFavorite_WithoutAccountId(){
        Account accountDefault = AccountFactory.createDefaultAccount();
        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();
        Recipe createdRecipe = this.recipeRepository.save(recipeDefault);

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.accountService.addRecipeToFavorite(accountDefault, createdRecipe));

        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testAddRecipeToFavorite_WithoutRecipeId(){
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account savedAccount = this.accountService.createAccount(accountDefault);
        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.accountService.addRecipeToFavorite(savedAccount, recipeDefault));

        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testAddRecipeToFavorite_WhenRecipeDoesNotExist(){
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account savedAccount = this.accountService.createAccount(accountDefault);

        Recipe recipeDefault = RecipeFactory.createRecipeWithId(UUID.randomUUID());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.accountService.addRecipeToFavorite(savedAccount, recipeDefault));

        assertEquals(ExceptionsMessages.RECIPE_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testAddRecipeToFavorite_WhenAccountDoesNotExist(){
        Account accountDefault = AccountFactory.createAccountWithId(UUID.randomUUID());

        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();
        Recipe savedRecipe = this.recipeRepository.save(recipeDefault);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.accountService.addRecipeToFavorite(accountDefault, savedRecipe));

        assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
    }
    //TODO Tests si la recette n'existe pas, si le compte n'existe pas

    @Test
    public void testAddIngredientToFridgeSuccess(){
        Account accountCreated = this.accountService.createAccount(AccountFactory.createDefaultAccount());
        Ingredient ingredientCreated = this.ingredientRepository.save(IngredientFactory.createDefaultIngredient());
        int quantity = 2;

        boolean result = this.accountService.addIngredientToFridge(ingredientCreated, accountCreated, quantity);

        List<Fridge> fridges = this.fridgeRepository.findAll();

        assertTrue(result);
        assertEquals(1, fridges.size());
        assertNotNull(fridges.get(0));
        assertEquals(accountCreated.getId(), fridges.get(0).getAccount().getId());
        assertEquals(ingredientCreated.getId(), fridges.get(0).getIngredient().getId());
        assertEquals(quantity, fridges.get(0).getQuantity());
    }

    @Test
    public void testAddIngredientToFridge_WithoutIngredientId(){
        Account account = AccountFactory.createDefaultAccount();
        Account savedAccount = this.accountService.createAccount(account);
        int quantity = 5;
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.accountService.addIngredientToFridge(ingredient, savedAccount, quantity));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testAddIngredientToFridge_WhenIngredientDoesNotExist(){
        Account account = AccountFactory.createDefaultAccount();
        Account savedAccount = this.accountService.createAccount(account);

        Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(UUID.randomUUID());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.accountService.addIngredientToFridge(ingredient, savedAccount, 5));

        assertEquals(ExceptionsMessages.INGREDIENT_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testAddIngredientToFridge_WithoutAccountId(){
        Account accountDefault = AccountFactory.createDefaultAccount();
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientRepository.save(ingredient);

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> this.accountService.addIngredientToFridge(savedIngredient, accountDefault, 5));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testAddIngredientToFridge_WhenAccountDoesNotExist(){
        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = this.ingredientRepository.save(ingredient);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> this.accountService.addIngredientToFridge(savedIngredient, account, 5));
        assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    public void testAddIngredientToFridge_WhenIngredientAlreadyAdded(){
        Account accountCreated = this.accountService.createAccount(AccountFactory.createDefaultAccount());
        Ingredient ingredientCreated = this.ingredientRepository.save(IngredientFactory.createDefaultIngredient());

        this.accountService.addIngredientToFridge(ingredientCreated, accountCreated, 5);
        ConflictException thrown = assertThrows(ConflictException.class , () -> this.accountService.addIngredientToFridge(ingredientCreated, accountCreated, 5));
        assertEquals(ExceptionsMessages.INGREDIENT_ALREADY_ADDED, thrown.getMessage());
    }


    @Test
    public void testGetFridge_WhenFridgeExists() {
        Ingredient ingredient = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        Ingredient ingredient2 = IngredientFactory.createDefaultIngredient();
        Ingredient savedIngredient2 = ingredientRepository.save(ingredient2);
        Account account = AccountFactory.createAccountWithId(UUID.fromString("1083349f-d171-4e59-9769-e073222f96d9"));
        Account savedAccount = accountRepository.save(account);
        int quantity = 5;

        accountService.addIngredientToFridge(savedIngredient, savedAccount, quantity);
        accountService.addIngredientToFridge(savedIngredient2, savedAccount, quantity);

        List<Fridge> fridge = accountService.getFridges(savedAccount.getId().toString());
        assertEquals(2, fridge.size());
    }

    @Test
    public void testGetFridge_WithoutAccountId() {
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.getFridges(null));
        assertEquals(ExceptionsMessages.WRONG_PARAMETERS, thrown.getMessage());
    }

    @Test
    public void testGetFridge_WhenAccountDoesNotExist() {
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.getFridges(UUID.randomUUID().toString()));
        assertEquals(ExceptionsMessages.ACCOUNT_DOES_NOT_EXIST, thrown.getMessage());
    }
}
