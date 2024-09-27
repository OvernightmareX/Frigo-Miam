package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.exceptions.NullParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import com.example.FrigoMiamBack.utils.JwtUtils;
import com.example.FrigoMiamBack.utils.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsTest {

    @Nested
    class GenerateTokenTest{
        @Test
        public void ShouldHaveToken_WithCorrectEmail_AndPassword_AndValid() throws Exception{
            Account account = AccountFactory.createAccountWithId(UUID.randomUUID());

            String token = JwtUtils.generateToken(account, Role.USER);

            assertEquals(account.getEmail(), JwtUtils.extractEmail(token));
            assertEquals(Role.USER.toString(), JwtUtils.extractRole(token));
            assertTrue(JwtUtils.validateToken(token, account));
        }

        @Test
        public void ShouldThrowNullParameterException_WhenAccountIsNull() throws Exception{
            Role role = Role.USER;

            assertThrows(NullParameterException.class, () -> {
                JwtUtils.generateToken(null, role);
            });
        }

        @Test
        public void ShouldThrowNullParameterException_WhenRoleIsNull() throws Exception{
            Account account = AccountFactory.createAccountWithId(UUID.randomUUID());

            assertThrows(NullParameterException.class, () -> {
                JwtUtils.generateToken(account, null);
            });
        }
    }

    @Nested
    class ValidateTokenTest{
        @Test
        public void ShouldReturnTrue_WhenTokenIsValid() throws Exception{
            Account account = AccountFactory.createAccountWithId(UUID.randomUUID());
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);

            assertTrue(JwtUtils.validateToken(token, account));
        }

        @Test
        public void ShouldReturnFalse_WhenEmailInvalid() throws Exception{
            Account account = AccountFactory.createAccountWithEmail("test@email.com");
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);
            account.setEmail("autre@gmail.com");

            assertFalse(JwtUtils.validateToken(token, account));
        }

        @Test
        public void ShouldThrowNullParameterException_WhenAccountIsNull() throws Exception{
            Account account = AccountFactory.createAccountWithEmail("test@email.com");
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);

            assertThrows(NullParameterException.class, () -> {
                JwtUtils.validateToken(token, null);
            });
        }

        @Test
        public void ShouldThrowNullParameterException_WhenTokenIsNull() throws Exception{
            Account account = AccountFactory.createAccountWithEmail("test@email.com");

            assertThrows(NullParameterException.class, () -> {
                JwtUtils.validateToken(null, account);
            });
        }
        @Test
        public void ShouldHaveSameEmail_WhenExtractFromToken() throws Exception{
            Account account = AccountFactory.createAccountWithEmail("test@email.com");
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);

            assertEquals(account.getEmail() , JwtUtils.extractEmail(token));
        }
        @Test
        public void ShouldThrowNullParameterException_WhenExtractEmail_AndTokenIsNull() throws Exception{
            assertThrows(NullParameterException.class, () -> {
                JwtUtils.extractEmail(null);
            });
        }

        @Test
        public void ShouldHaveSameRole_WhenExtractFromToken() throws Exception{
            Account account = AccountFactory.createDefaultAccount();
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);

            assertEquals(role.toString() ,JwtUtils.extractRole(token));
        }

        @Test
        public void ShouldThrowNullParameterException_WhenExtractRole_AndTokenIsNull() throws Exception{
            assertThrows(NullParameterException.class, () -> {
                JwtUtils.extractRole(null);
            });
        }

        @Test
        public void ShouldReturnExpirationDateOneDayLater_WhenExtractFromToken() throws Exception{
            Account account = AccountFactory.createDefaultAccount();
            Role role = Role.USER;

            String token = JwtUtils.generateToken(account, role);
            Date tokenTime = JwtUtils.extractExpiration(token);
            LocalDateTime localDateTime = tokenTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            assertEquals(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).getDayOfMonth(), localDateTime.getDayOfMonth());
            assertEquals(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).getHour(), localDateTime.getHour());
        }

        @Test
        public void ShouldThrowNullParameterException_WhenExtractExpiration_AndTokenIsNull() throws Exception{
            assertThrows(NullParameterException.class, () -> {
                JwtUtils.extractExpiration(null);
            });
        }
    }
}