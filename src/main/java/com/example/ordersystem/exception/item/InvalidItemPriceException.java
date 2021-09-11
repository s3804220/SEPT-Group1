package com.example.ordersystem.exception.item;

import java.text.MessageFormat;

public class InvalidItemPriceException extends RuntimeException{
    public InvalidItemPriceException(String price){
        super(MessageFormat.format("Item price {0} is invalid", price));
    }
}
