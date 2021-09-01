package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

public class CreatedEmail extends Email{

    @Override
    public String getSubject() {
        return "Your order has been created!";
    }

    @Override
    public String getOrderStatus() {
        return "has been created.";
    }

    @Override
    public String getEmailContent() {
        return "<b>Dear customer</b>,<br><br>Your order has been created and is awaiting an Admin's confirmation."
                +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";
    }

    @Override
    public ArrayList<FileSystemResource> getResources() {
        ArrayList<FileSystemResource> newArray = super.getResources();
        newArray.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status1.jpg".replace("\\", File.separator))));
        return newArray;
    }
}
