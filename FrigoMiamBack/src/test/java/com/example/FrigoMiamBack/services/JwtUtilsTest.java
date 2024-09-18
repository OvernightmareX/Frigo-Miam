package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.entities.Role;
import com.example.FrigoMiamBack.exceptions.NullParameterException;
import com.example.FrigoMiamBack.factories.AccountFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtilsService;

    @Test
    public void testGenerateTokenSuccess() throws Exception{
        String resultExpected_2 = "USER";

        Role role = Role.builder()
                .name(resultExpected_2)
                .build();

        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());

        String token = this.jwtUtilsService.generateToken(account, role);

        assertEquals(account.getEmail(), this.jwtUtilsService.extractEmail(token));
        assertEquals(resultExpected_2, this.jwtUtilsService.extractRole(token));
        assertTrue(this.jwtUtilsService.validateToken(token, account));
    }

    @Test
    public void testGenerateToken_WhenAccountIsNull_ThenFail() throws Exception{
        Role role = Role.builder()
                .name("USER")
                .build();

        assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.generateToken(null, role);
        });
    }

    @Test
    public void testGenerateToken_WhenRoleIsNull_ThenFail() throws Exception{
        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());

        assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.generateToken(account, null);
        });
    }


    @Test
    public void testValidateTokenSuccess() throws Exception{
        Account account = AccountFactory.createAccountWithId(UUID.randomUUID());

        Role role = Role.builder()
                .name("USER")
                .build();

        String token = this.jwtUtilsService.generateToken(account, role);

        assertTrue(this.jwtUtilsService.validateToken(token, account));
    }



/*
    @Test
    public void testValidateToken_WhenEmailInvalid() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        String token = this.jwtUtilsService.generateToken(userInfoDTO);

        userInfoDTO.setEmail("autre@gmail.com");

        Assert.assertFalse(this.jwtUtilsService.validateToken(token, userInfoDTO));
    }

    @Test
    public void verification_test_validate_user_null() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        String token = this.jwtUtilsService.generateToken(userInfoDTO);

        Assert.assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.validateToken(token, null);
        });
    }

    @Test
    public void verification_test_validate_token_null() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        Assert.assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.validateToken(null, userInfoDTO);
        });
    }

    @Test
    public void verification_test_extractEmail_ok() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        String token = this.jwtUtilsService.generateToken(userInfoDTO);

        Assert.assertEquals(userInfoDTO.getEmail() ,this.jwtUtilsService.extractEmail(token));
    }

    @Test
    public void verification_test_extractEmail_token_null() throws Exception{
        Assert.assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.extractEmail(null);
        });
    }

    @Test
    public void verification_test_extractPassword_ok() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        String token = this.jwtUtilsService.generateToken(userInfoDTO);

        Assert.assertEquals(userInfoDTO.getPassword() ,this.jwtUtilsService.extractPassword(token));
    }

    @Test
    public void verification_test_extractPassword_token_null() throws Exception{
        Assert.assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.extractEmail(null);
        });
    }

    @Test
    public void verification_test_extractExpiration_ok() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO(UUID.randomUUID(), "loick@gmail.com", "monPetitPassword");

        String token = this.jwtUtilsService.generateToken(userInfoDTO);
        Date tokenTime = this.jwtUtilsService.extractExpiration(token);
        LocalDateTime localDateTime = tokenTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Assert.assertEquals(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).getDayOfMonth() , localDateTime.getDayOfMonth());
        Assert.assertEquals(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).getHour() , localDateTime.getHour());
    }

    @Test
    public void verification_test_extractExpiration_token_null() throws Exception{
        Assert.assertThrows(NullParameterException.class, () -> {
            this.jwtUtilsService.extractEmail(null);
        });
    }*/
}