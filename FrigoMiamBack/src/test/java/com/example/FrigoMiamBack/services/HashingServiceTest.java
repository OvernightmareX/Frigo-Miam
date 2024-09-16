package com.example.FrigoMiamBack.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HashingServiceTest {
    @Autowired
    private HashingService hashingService;

    @Test
    public void testHashPasswordSuccess() throws Exception{
        String password = "password";

        String hashedPassword = this.hashingService.hashPassword(password);

        assertNotEquals(hashedPassword, password);
        assertTrue(this.hashingService.verifyPassword(password, hashedPassword));
    }

    @Test
    public void testHashPassword_WhenSamePassword_ThenDifferentHashedPassword() throws Exception{
        String password = "password";

        String hashedPassword = this.hashingService.hashPassword(password);
        String hashedPassword_2 = this.hashingService.hashPassword(password);

        assertNotEquals(hashedPassword, password);
        assertNotEquals(hashedPassword, hashedPassword_2);
    }

    @Test
    public void testVerifyPassword_WhenCorrectPassword() throws Exception{
        String password = "password";

        String hashedPassword = this.hashingService.hashPassword("password");

        assertNotEquals(hashedPassword, password);
        assertTrue(this.hashingService.verifyPassword(password, hashedPassword));
    }

    @Test
    public void testVerifyPassword_WhenWrongPassword() throws Exception{
        String password = "password";

        String hashedPassword = this.hashingService.hashPassword("wrong password");

        assertNotEquals(hashedPassword, password);
        assertFalse(this.hashingService.verifyPassword(password, hashedPassword));
    }
}