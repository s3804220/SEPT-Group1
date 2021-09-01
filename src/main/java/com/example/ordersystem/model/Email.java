package com.example.ordersystem.model;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;

public abstract class Email {
    public abstract String getSubject();
    public abstract String getOrderStatus();
    public abstract String getEmailContent();
    public ArrayList<FileSystemResource> getResources(){
        ArrayList<FileSystemResource> array = new ArrayList<>();
        array.add(new FileSystemResource(new File("target\\classes\\static\\img\\logo.png".replace("\\", File.separator))));
        array.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\facebook.png".replace("\\", File.separator))));
        array.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\twitter.png".replace("\\", File.separator))));
        array.add(new FileSystemResource(new File("target\\classes\\static\\img\\contact\\instagram.png".replace("\\", File.separator))));
        array.add(new FileSystemResource(new File("target\\classes\\static\\img\\hero\\hero-1.jpg".replace("\\", File.separator))));
        return array;
    }
}
