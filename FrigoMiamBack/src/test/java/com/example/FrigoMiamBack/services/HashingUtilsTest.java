package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.utils.HashingUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HashingUtilsTest {
    @Nested
    class HashPasswordTest{
        @Test
        public void ShouldHashPassword() throws Exception{
            String password = "password";

            String hashedPassword = HashingUtils.hashPassword(password);

            assertNotEquals(hashedPassword, password);
            assertTrue(HashingUtils.verifyPassword(password, hashedPassword));
        }
        @Test
        public void ShouldHaveDifferentHashedPassword_WhenSamePassword() throws Exception{
            String password = "password";

            String hashedPassword = HashingUtils.hashPassword(password);
            String hashedPassword_2 = HashingUtils.hashPassword(password);

            assertNotEquals(hashedPassword, password);
            assertNotEquals(hashedPassword, hashedPassword_2);
        }
    }

    @Nested
    class VerifyPassword{
        @Test
        public void ShouldReturnTrue_WhenCorrectPassword() throws Exception{
            String password = "password";

            String hashedPassword = HashingUtils.hashPassword("password");

            assertNotEquals(hashedPassword, password);
            assertTrue(HashingUtils.verifyPassword(password, hashedPassword));
        }

        @Test
        public void ShouldReturnFalse_WhenWrongPassword() throws Exception{
            String password = "password";

            String hashedPassword = HashingUtils.hashPassword("wrong password");

            assertNotEquals(hashedPassword, password);
            assertFalse(HashingUtils.verifyPassword(password, hashedPassword));
        }
    }
}