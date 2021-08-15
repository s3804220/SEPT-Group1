package com.example.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Transactional
@Service
public class EmailService{
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String receiver, String status){
        String sender = "sept.system1@gmail.com";

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(receiver);

            switch (status){
                case "created":
                    //Send email to recipient when their order has been created
                    helper.setSubject("Your order has been created!");
                    String content = "<b>Dear customer</b>,<br>Your order has been created and is awaiting an Admin's confirmation."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us."
                            + "<br><img src='cid:status1' style='max-width: 100%;'/><br><br><b>Thank you for your purchase!</b>"
                            +"<br><br><img src='cid:logo'/>";
                    helper.setText(content, true);

                    FileSystemResource resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status1.jpg".replace("\\", File.separator)));
                    helper.addInline("status1", resource);
                    break;
                case "confirmed":
                    //Send email to recipient when their order has been confirmed
                    break;
                case "processed":
                    //Send email to recipient when their order has been processed
                    break;
                case "delivering":
                    //Send email to recipient when their order is being delivered
                    break;
                case "delivered":
                    //Send email to recipient when their order has been delivered
                    break;
                case "cancelled":
                    //Send email to recipient when their order has been cancelled
                    break;
            }

            FileSystemResource resource1 = new FileSystemResource(new File("target\\classes\\static\\img\\logo.png".replace("\\", File.separator)));
            helper.addInline("logo", resource1);
            mailSender.send(message);

        } catch (MessagingException e){
            System.out.println("Cannot send email to "+receiver);
        }

    }

}
