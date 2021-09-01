package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

public class CancelledEmail extends Email{
    @Override
    public String getSubject() {
        return "Your order has been cancelled.";
    }

    @Override
    public String getOrderStatus() {
        return "has been cancelled.";
    }

    @Override
    public String getEmailContent() {
        return "<b>Dear customer</b>,<br><br>This email is to confirm that your order has been cancelled."
                +"<br>Please contact an Administrator if you think this is an error.<br>We apologize for any inconvenience.";
    }

    @Override
    public ArrayList<FileSystemResource> getResources() {
        ArrayList<FileSystemResource> newArray = super.getResources();
        newArray.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\ordercancelled.jpg".replace("\\", File.separator))));
        return newArray;
    }
}
