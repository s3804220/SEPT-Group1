package com.example.ordersystem.exception.item;

import java.text.MessageFormat;

public class InvalidItemDescriptionException extends RuntimeException{
    public InvalidItemDescriptionException(String description){
        super(MessageFormat.format("Item description {0} is invalid", description));
    }
}
