package com.example.ordersystem.exception.account;

import java.text.MessageFormat;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(String email){
        super(MessageFormat.format("Email \"{0}\" has an invalid format", email));
    }
}
