package com.example.ordersystem.exception.account;
import java.text.MessageFormat;

public class InvalidPhoneFormatException extends RuntimeException{
    public InvalidPhoneFormatException(String phone){
        super(MessageFormat.format("Phone number \"{0}\" has an invalid format", phone));
    }
}
