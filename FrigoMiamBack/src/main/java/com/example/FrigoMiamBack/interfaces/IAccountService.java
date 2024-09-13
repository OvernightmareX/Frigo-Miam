package com.example.FrigoMiamBack.interfaces;

import com.example.FrigoMiamBack.entities.Account;

public interface IAccountService {

    boolean checkEmail(String email);

    boolean createAccount(Account accountToCreate);



}
