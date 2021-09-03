package com.example.ordersystem.exception.item;

import java.text.MessageFormat;

public class InvalidItemNameException extends RuntimeException{
    public InvalidItemNameException(String name){
        super(MessageFormat.format("Item name {0} is invalid", name));
    }
}
