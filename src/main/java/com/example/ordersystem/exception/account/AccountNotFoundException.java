package com.example.ordersystem.exception.account;

import java.text.MessageFormat;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long id){
        super(MessageFormat.format("Unable to find account with id {0}", id));
    }
}
