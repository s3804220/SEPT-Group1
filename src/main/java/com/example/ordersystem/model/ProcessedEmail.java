package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

/**
 * This class inherits from the abstract Email class
 * It is used to create an email with subject and content specific to the processed order status
 */
public class ProcessedEmail extends Email{
    @Override
    public String getSubject() {
        return "Your order has been processed!";
    }

    @Override
    public String getOrderStatus() {
        return "has been processed.";
    }

    @Override
    public String getEmailContent() {
        return "<b>Dear customer</b>,<br><br>Your order has been processed and is now waiting to be delivered to you."
                +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";
    }

    @Override
    public ArrayList<FileSystemResource> getResources() {
        ArrayList<FileSystemResource> newArray = super.getResources();
        newArray.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status3.jpg".replace("\\", File.separator))));
        return newArray;
    }
}
