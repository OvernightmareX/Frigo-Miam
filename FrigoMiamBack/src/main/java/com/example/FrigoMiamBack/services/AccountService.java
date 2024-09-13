package com.example.FrigoMiamBack.services;

import com.example.FrigoMiamBack.entities.Account;
import com.example.FrigoMiamBack.interfaces.IAccountService;

public class AccountService implements IAccountService {
    @Override
    public boolean checkEmail(String email) {
        return false;
    }

    @Override
    public boolean createAccount(Account accountToCreate) {
        return false;
    }


}
