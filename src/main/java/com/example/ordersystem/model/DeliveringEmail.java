package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

public class DeliveringEmail extends Email{
    @Override
    public String getSubject() {
        return "Your order is being delivered!";
    }

    @Override
    public String getOrderStatus() {
        return "is being delivered.";
    }

    @Override
    public String getEmailContent() {
        return "<b>Dear customer</b>,<br><br>Your order is now on its way to reach you!"
                +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";
    }

    @Override
    public ArrayList<FileSystemResource> getResources() {
        ArrayList<FileSystemResource> newArray = super.getResources();
        newArray.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status4.jpg".replace("\\", File.separator))));
        return newArray;
    }
}
