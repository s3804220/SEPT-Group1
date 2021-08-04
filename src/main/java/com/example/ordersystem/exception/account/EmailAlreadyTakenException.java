package com.example.ordersystem.exception.account;

import java.text.MessageFormat;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String email){
        super(MessageFormat.format("Email {0} is already taken", email));
    }
}
