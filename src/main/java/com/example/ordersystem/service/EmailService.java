package com.example.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Transactional
@Service
public class EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine emailTemplateEngine;

    public void sendEmail(String receiver, String status){
        String sender = "sept.system1@gmail.com";
        String subject = "", content = "";

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(receiver);

            Context thymeleafContext = new Context();

            FileSystemResource resource = new FileSystemResource(new File(""));

            switch (status){
                case "created":
                    //Send email to recipient when their order has been created
                    subject = "Your order has been created!";
                    thymeleafContext.setVariable("orderstatus","has been created.");
                    content = "<b>Dear customer</b>,<br><br>Your order has been created and is awaiting an Admin's confirmation."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status1.jpg".replace("\\", File.separator)));
                    break;
                case "confirmed":
                    //Send email to recipient when their order has been confirmed
                    subject = "Your order has been confirmed!";
                    thymeleafContext.setVariable("orderstatus","has been confirmed.");
                    content = "<b>Dear customer</b>,<br><br>Your order has been confirmed and is now waiting to be processed."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status2.jpg".replace("\\", File.separator)));
                    break;
                case "processed":
                    //Send email to recipient when their order has been processed
                    subject = "Your order has been processed!";
                    thymeleafContext.setVariable("orderstatus","has been processed.");
                    content = "<b>Dear customer</b>,<br><br>Your order has been processed and is now waiting to be delivered to you."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status3.jpg".replace("\\", File.separator)));
                    break;
                case "delivering":
                    //Send email to recipient when their order is being delivered
                    subject = "Your order is being delivered!";
                    thymeleafContext.setVariable("orderstatus","is being delivered.");
                    content = "<b>Dear customer</b>,<br><br>Your order is now on its way to reach you!"
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status4.jpg".replace("\\", File.separator)));
                    break;
                case "delivered":
                    //Send email to recipient when their order has been delivered
                    subject = "Your order has been delivered successfully!";
                    thymeleafContext.setVariable("orderstatus","has been delivered successfully.");
                    content = "<b>Dear customer</b>,<br><br>This email is to confirm that your order has been delivered successfully."
                            +"<br>Please check your order status below.<br>Our system will now mark your order as complete.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status5.jpg".replace("\\", File.separator)));
                    break;
                case "cancelled":
                    //Send email to recipient when their order has been cancelled
                    subject = "Your order has been cancelled.";
                    thymeleafContext.setVariable("orderstatus","has been cancelled.");
                    content = "<b>Dear customer</b>,<br><br>This email is to confirm that your order has been cancelled."
                            +"<br>Please contact an Administrator if you think this is an error.<br>We apologize for any inconvenience.";

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\ordercancelled.jpg".replace("\\", File.separator)));
                    break;
            }

            //Set the subject of the email to be sent
            helper.setSubject(subject);
            thymeleafContext.setVariable("mailContent",content);
            thymeleafContext.setVariable("orderNum",1);
            thymeleafContext.setVariable("orderTotal",999);

            String mailTemplate = emailTemplateEngine.process("email-template.html",thymeleafContext);
            helper.setText(mailTemplate, true);

            helper.addInline("status", resource);
            FileSystemResource logo = new FileSystemResource(new File("target\\classes\\static\\img\\logo.png".replace("\\", File.separator)));
            helper.addInline("logo", logo);
            FileSystemResource logo1 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\facebook.png".replace("\\", File.separator)));
            helper.addInline("facebook", logo1);
            FileSystemResource logo2 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\twitter.png".replace("\\", File.separator)));
            helper.addInline("twitter", logo2);
            FileSystemResource logo3 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\instagram.png".replace("\\", File.separator)));
            helper.addInline("instagram", logo3);
            FileSystemResource footer = new FileSystemResource(new File("target\\classes\\static\\img\\hero\\hero-1.jpg".replace("\\", File.separator)));
            helper.addInline("footer", footer);

            mailSender.send(message);

        } catch (MessagingException e){
            System.out.println("Cannot send email to "+receiver);
        }

    }

}
