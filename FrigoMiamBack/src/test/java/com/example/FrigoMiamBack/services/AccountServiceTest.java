package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.entities.Recipe;
import com.example.FrigoMiamBack.exceptions.ConflictException;
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

    @BeforeEach
    public void setup() {
        accountService = new AccountService(accountRepository, fridgeRepository);
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

        //TODO add test for other attributes
        assertNotNull(createdAccount.getId());
        assertEquals(account.getFirstname(), createdAccount.getFirstname());
        assertEquals(account.getLastname(), createdAccount.getLastname());
        assertEquals(account.getPassword(), createdAccount.getPassword());
        assertEquals(account.getEmail(), createdAccount.getEmail());
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
    public void testAddRecipeToFavoriteSuccess() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account createdAccount = this.accountService.createAccount(accountDefault);

        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();
        Recipe createdRecipe = this.recipeRepository.save(recipeDefault);

        Account accountUpdated = this.accountService.addRecipeToFavorite(createdAccount, createdRecipe);

        assertEquals(1, accountUpdated.getRecipeLikedList().size());
        assertEquals(createdRecipe, accountUpdated.getRecipeLikedList().get(0));
    }

    //TODO add test to AddRecipeToFavorite (when account.id is null, when recipe.id is null, etc

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

    //TODO add more tests to AddIngredientToFridge
}
