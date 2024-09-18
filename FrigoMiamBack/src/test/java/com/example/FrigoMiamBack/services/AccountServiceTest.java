package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.DTO.IngredientQuantityDTO;
import com.example.FrigoMiamBack.DTO.TokenDTO;
import com.example.FrigoMiamBack.entities.*;
import com.example.FrigoMiamBack.exceptions.ConflictException;
import com.example.FrigoMiamBack.exceptions.NotFoundException;
import com.example.FrigoMiamBack.exceptions.WrongParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.factories.IngredientFactory;
import com.example.FrigoMiamBack.factories.RecipeFactory;
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
public class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RoleRepository roleRepository;

    private RecipeService recipeService;
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        recipeService = new RecipeService(recipeRepository, accountRepository, ingredientRepository);
        accountService = new AccountService(accountRepository, recipeRepository, ingredientRepository, roleRepository);
    }

    @Nested
    class CheckEmailTest {
        @Test
        public void ShouldReturnTrue_WhenEmailExists() {
            String emailSearched = "email@test.com";
            Account account = AccountFactory.createAccountWithEmail(emailSearched);
            accountRepository.save(account);

            assertTrue(accountService.checkEmail(emailSearched));
        }

        @Test
        public void ShouldReturnFalse_WhenEmailNotExists() {
            assertFalse(accountService.checkEmail("emailNotExist@test.fr"));
        }
    }

    @Nested
    class CreateAccountTest {
        @Test
        public void ShouldHaveCreatedAccount_WithCorrectValues() {
            Account account = AccountFactory.createDefaultAccount();
            String password = account.getPassword();
            Account createdAccount = accountService.createAccount(account);

            assertNotNull(createdAccount.getId());
            assertEquals(account.getFirstname(), createdAccount.getFirstname());
            assertEquals(account.getLastname(), createdAccount.getLastname());
            assertTrue(HashingUtils.verifyPassword(password, createdAccount.getPassword()));
            assertEquals(account.getEmail(), createdAccount.getEmail());
            assertEquals(account.getBirthdate(), createdAccount.getBirthdate());
            assertEquals(account.getAllergies(), createdAccount.getAllergies());
            assertEquals(account.getDiet(), createdAccount.getDiet());
        }

        @Test
        public void ShouldThrowConflictException_WhenEmailAlreadyExist() {
            String emailTest = "already@exist.com";
            Account existingAccount = AccountFactory.createAccountWithEmail(emailTest);
            accountRepository.save(existingAccount);

            Account anotherAccount = AccountFactory.createAccountWithEmail(emailTest);

            ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(anotherAccount));

            assertEquals(ExceptionsMessages.EMAIL_ALREADY_EXIST, thrown.getMessage());
        }

        @Test
        public void ShouldThrowConflictException_WhenAccountAlreadyExist() {
            UUID accountId = UUID.randomUUID();
            Account accountWithId = AccountFactory.createAccountWithId(accountId);

            ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.createAccount(accountWithId));

            assertEquals(ExceptionsMessages.ACCOUNT_ALREADY_CREATED, thrown.getMessage());
        }
    }

    @Nested
    class LogInTest {

        @Test
        public void ShouldLogInSuccessfully_WithCorrectCredentials() {
            String email = "test@example.com";
            String password = "correctPassword";
            Account account = AccountFactory.createDefaultAccount();
            account.setEmail(email);
            account.setPassword(password);

            accountService.createAccount(account);

            Role role = Role.builder().name("USER").build();
            roleRepository.save(role);

            TokenDTO token = accountService.logIn(email, password);

            assertNotNull(token);
            assertTrue(JwtUtils.validateToken(token.getToken(), account));
        }

        @Test
        public void ShouldThrowNotFoundException_WhenEmailDoesNotExist() {
            String email = "nonexistent@example.com";
            String password = "password";

            NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.logIn(email, password));

            assertEquals(ExceptionsMessages.ACCOUNT_TO_LOGIN_DOES_NOT_EXIST, thrown.getMessage());
        }

        @Test
        public void ShouldThrowRuntimeException_WhenPasswordIsIncorrect() {
            // Arrange
            String email = "test@example.com";
            String correctPassword = "correctPassword";
            String wrongPassword = "wrongPassword";

            Account account = AccountFactory.createDefaultAccount();
            account.setEmail(email);
            account.setPassword(HashingUtils.hashPassword(correctPassword)); // Store the hashed correct password

            accountService.createAccount(account);

            assertThrows(RuntimeException.class, () -> accountService.logIn(email, wrongPassword));
        }
    }


    @Test
    public void testGetAccountById_WithoutAccountId() {
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> {
            accountService.getAccountById(null);
        });
        assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_FIND_ACCOUNT, thrown.getMessage());
    }

    @Test
    public void testGetAccountById_WithAccountId() {
        Account account = AccountFactory.createDefaultAccount();
        Account saved = accountService.createAccount(account);

        Account found = accountService.getAccountById(saved.getId());

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
    public void testUpdateAccount_WithoutAccountId() {
        Account account = AccountFactory.createDefaultAccount();
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_UPDATE_ACCOUNT, thrown.getMessage());
    }

    @Test
    public void testUpdateAccount_WhenAccountDoesNotExist() {
        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_UPDATE, thrown.getMessage());
    }

    @Test
    public void testDeleteAccountSuccess() {
        Account account = AccountFactory.createDefaultAccount();
        accountService.createAccount(account);
        boolean result = accountService.deleteAccount(account);
        assertTrue(result);
    }

    @Test
    public void testDeleteAccount_WithoutAccountId() {
        Account account = AccountFactory.createDefaultAccount();
        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.deleteAccount(account));
        assertEquals(ExceptionsMessages.EMPTY_ID_CANNOT_DELETE_ACCOUNT, thrown.getMessage());
    }

    @Test
    public void testDeleteAccount_WhenAccountDoesNotExist() {
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.deleteAccount(AccountFactory.createAccountWithId(UUID.randomUUID())));
        assertEquals(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_DELETE, thrown.getMessage());
    }

    @Test
    public void testAddRecipeToFavoriteSuccess() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account createdAccount = this.accountService.createAccount(accountDefault);
    }

    @Nested
    class AddRecipeTest {
        @Test
        public void ShouldHaveARecipeInAccount_WhenAdded() {
            Account accountDefault = AccountFactory.createDefaultAccount();
            Account createdAccount = accountService.createAccount(accountDefault);

            Recipe recipeDefault = RecipeFactory.createDefaultRecipe();

            Recipe createdRecipe = recipeRepository.save(recipeDefault);

            Account accountUpdated = accountService.addRecipeToFavorite(createdAccount, createdRecipe);

            assertEquals(1, accountUpdated.getRecipeLikedList().size());
            assertEquals(createdRecipe, accountUpdated.getRecipeLikedList().get(0));
        }
    }


    @Test
    public void ShouldThrowWrongParameterException_WithoutAccountId() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();

        List<IngredientQuantityDTO> ingredients = new ArrayList<>();
        Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));

        Recipe createdRecipe = recipeRepository.save(recipeDefault);

        System.out.println(createdRecipe);

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.addRecipeToFavorite(accountDefault, createdRecipe));

        assertEquals(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_ADD_RECIPE_TO_FAVORITE, thrown.getMessage());
    }


    @Test
    public void ShouldThrowWrongParameterException_WithoutRecipeId() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account savedAccount = accountService.createAccount(accountDefault);
        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();

        WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.addRecipeToFavorite(savedAccount, recipeDefault));

        assertEquals(ExceptionsMessages.EMPTY_RECIPE_ID_CANNOT_ADD_RECIPE_TO_FAVORITE, thrown.getMessage());
    }

    @Test
    public void ShouldThrowNotFoundException_WhenRecipeDoesNotExist() {
        Account accountDefault = AccountFactory.createDefaultAccount();
        Account savedAccount = accountService.createAccount(accountDefault);

        Recipe recipeDefault = RecipeFactory.createRecipeWithId(UUID.randomUUID());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.addRecipeToFavorite(savedAccount, recipeDefault));

        assertEquals(ExceptionsMessages.NO_RECIPE_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE, thrown.getMessage());
    }

    @Test
    public void ShouldThrowNotFoundException_WhenAccountDoesNotExist() {
        Account accountDefault = AccountFactory.createAccountWithId(UUID.randomUUID());

        Recipe recipeDefault = RecipeFactory.createDefaultRecipe();

        List<IngredientQuantityDTO> ingredients = new ArrayList<>();
        Ingredient ingredient = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));
        ingredients.add(new IngredientQuantityDTO(ingredient, 5));

        Recipe savedRecipe = recipeRepository.save(recipeDefault);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.addRecipeToFavorite(accountDefault, savedRecipe));

        assertEquals(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_ADD_RECIPE_TO_FAVORITE, thrown.getMessage());
    }

    @Nested
    class AddIngredientTest {
        @Test
        public void ShouldHaveIngredientInFridgeWithSameValues() {
            Account accountCreated = accountService.createAccount(AccountFactory.createDefaultAccount());
            Ingredient ingredientCreated = ingredientRepository.save(IngredientFactory.createDefaultIngredient());
            int quantity = 2;

            boolean result = accountService.addIngredientToFridge(ingredientCreated, accountCreated, quantity);

            List<Fridge> fridges = fridgeRepository.findAll();

            assertTrue(result);
            assertEquals(1, fridges.size());
            assertNotNull(fridges.get(0));
            assertEquals(accountCreated.getId(), fridges.get(0).getAccount().getId());
            assertEquals(ingredientCreated.getId(), fridges.get(0).getIngredient().getId());
            assertEquals(quantity, fridges.get(0).getQuantity());
        }

        @Test
        public void testAddIngredientToFridge_WithQuantityLowerThan_1() {
            Account accountCreated = accountService.createAccount(AccountFactory.createDefaultAccount());
            Ingredient ingredientCreated = ingredientRepository.save(IngredientFactory.createDefaultIngredient());

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.addIngredientToFridge(ingredientCreated, accountCreated, 0));

            assertEquals(ExceptionsMessages.QUANTITY_CANNOT_BE_ZERO_OR_LESS, thrown.getMessage());
        }

        @Test
        public void testAddIngredientToFridge_WithoutIngredientId() {
            Account account = AccountFactory.createDefaultAccount();
            Account savedAccount = accountService.createAccount(account);
            int quantity = 5;
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.addIngredientToFridge(ingredient, savedAccount, quantity));
            assertEquals(ExceptionsMessages.EMPTY_INGREDIENT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE, thrown.getMessage());
        }

        @Test
        public void testAddIngredientToFridge_WhenIngredientDoesNotExist() {
            Account account = AccountFactory.createDefaultAccount();
            Account savedAccount = accountService.createAccount(account);

            Ingredient ingredient = IngredientFactory.createIngredientWithCustomId(UUID.randomUUID());
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.addIngredientToFridge(ingredient, savedAccount, 5));

            assertEquals(ExceptionsMessages.NO_INGREDIENT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE, thrown.getMessage());
        }

        @Test
        public void testAddIngredientToFridge_WithoutAccountId() {
            Account accountDefault = AccountFactory.createDefaultAccount();
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientRepository.save(ingredient);

            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.addIngredientToFridge(savedIngredient, accountDefault, 5));
            assertEquals(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_ADD_INGREDIENT_TO_FRIDGE, thrown.getMessage());
        }

        @Test
        public void testAddIngredientToFridge_WhenAccountDoesNotExist() {
            Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
            Ingredient ingredient = IngredientFactory.createDefaultIngredient();
            Ingredient savedIngredient = ingredientRepository.save(ingredient);

            NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.addIngredientToFridge(savedIngredient, account, 5));
            assertEquals(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_ADD_INGREDIENT_TO_FRIDGE, thrown.getMessage());
        }

        @Test
        public void testAddIngredientToFridge_WhenIngredientAlreadyAdded() {
            Account accountCreated = accountService.createAccount(AccountFactory.createDefaultAccount());
            Ingredient ingredientCreated = ingredientRepository.save(IngredientFactory.createDefaultIngredient());

            accountService.addIngredientToFridge(ingredientCreated, accountCreated, 5);
            ConflictException thrown = assertThrows(ConflictException.class, () -> accountService.addIngredientToFridge(ingredientCreated, accountCreated, 5));
            assertEquals(ExceptionsMessages.INGREDIENT_ALREADY_ADDED_TO_FRIDGE, thrown.getMessage());
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

            List<Fridge> fridge = accountService.getFridges(savedAccount.getId());
            assertEquals(2, fridge.size());
        }

        @Test
        public void testGetFridge_WithoutAccountId() {
            WrongParameterException thrown = assertThrows(WrongParameterException.class, () -> accountService.getFridges(null));
            assertEquals(ExceptionsMessages.EMPTY_ACCOUNT_ID_CANNOT_FIND_FRIDGE, thrown.getMessage());
        }

        @Test
        public void testGetFridge_WhenAccountDoesNotExist() {
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> accountService.getFridges(UUID.randomUUID()));
            assertEquals(ExceptionsMessages.NO_ACCOUNT_FOUND_CANNOT_FIND_FRIDGE, thrown.getMessage());
        }

    }
}

