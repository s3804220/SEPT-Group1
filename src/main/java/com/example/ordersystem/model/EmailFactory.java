package com.example.ordersystem.model;

/**
 * This class is an email factory with a static method to return different types of emails
 * based on the order status that is passed to it
 */
public class EmailFactory {
    public static Email writeEmail(String status){
        switch (status) {
            case "created":
                return new CreatedEmail();
            case "confirmed":
                return new ConfirmedEmail();
            case "processed":
                return new ProcessedEmail();
            case "delivering":
                return new DeliveringEmail();
            case "delivered":
                return new DeliveredEmail();
            case "cancelled":
                return new CancelledEmail();
            default:
                return null;
        }
    }
}
