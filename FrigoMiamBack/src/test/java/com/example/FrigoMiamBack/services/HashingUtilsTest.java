package com.example.FrigoMiamBack.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HashingUtilsTest {

    @Test
    public void testHashPasswordSuccess() throws Exception{
        String password = "password";

        String hashedPassword = HashingUtils.hashPassword(password);

        assertNotEquals(hashedPassword, password);
        assertTrue(HashingUtils.verifyPassword(password, hashedPassword));
    }

    @Test
    public void testHashPassword_WhenSamePassword_ThenDifferentHashedPassword() throws Exception{
        String password = "password";

        String hashedPassword = HashingUtils.hashPassword(password);
        String hashedPassword_2 = HashingUtils.hashPassword(password);

        assertNotEquals(hashedPassword, password);
        assertNotEquals(hashedPassword, hashedPassword_2);
    }

    @Test
    public void testVerifyPassword_WhenCorrectPassword() throws Exception{
        String password = "password";

        String hashedPassword = HashingUtils.hashPassword("password");

        assertNotEquals(hashedPassword, password);
        assertTrue(HashingUtils.verifyPassword(password, hashedPassword));
    }

    @Test
    public void testVerifyPassword_WhenWrongPassword() throws Exception{
        String password = "password";

        String hashedPassword = HashingUtils.hashPassword("wrong password");

        assertNotEquals(hashedPassword, password);
        assertFalse(HashingUtils.verifyPassword(password, hashedPassword));
    }
}