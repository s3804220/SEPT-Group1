package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

/**
 * This class inherits from the abstract Email class
 * It is used to create an email with subject and content specific to the delivered order status
 */
public class DeliveredEmail extends Email{
    @Override
    public String getSubject() {
        return "Your order has been delivered successfully!";
    }

    @Override
    public String getOrderStatus() {
        return "has been delivered successfully.";
    }

    @Override
    public String getEmailContent() {
        return "<b>Dear customer</b>,<br><br>This email is to confirm that your order has been delivered successfully."
                +"<br>Please check your order status below.<br>Our system will now mark your order as complete.";
    }

    @Override
    public ArrayList<FileSystemResource> getResources() {
        ArrayList<FileSystemResource> newArray = super.getResources();
        newArray.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status5.jpg".replace("\\", File.separator))));
        return newArray;
    }
}
