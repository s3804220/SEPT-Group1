package com.example.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

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
                /*case "confirmed":
                    //Send email to recipient when their order has been confirmed
                    helper.setSubject("Your order has been confirmed!");
                    String content = "<b>Dear customer</b>,<br>Your order has been confirmed and is now waiting to be processed."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us."
                            + "<br><img src='cid:status2' style='max-width: 100%;'/><br><br><b>Thank you for your purchase!</b>"
                            +"<br><br><img src='cid:logo'/>";
                    helper.setText(content, true);

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status2.jpg".replace("\\", File.separator)));
                    helper.addInline("status2", resource);
                    break;
                case "processed":
                    //Send email to recipient when their order has been processed
                    helper.setSubject("Your order has been processed!");
                    content = "<b>Dear customer</b>,<br>Your order has been processed and is now waiting to be delivered to you."
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us."
                            + "<br><img src='cid:status3' style='max-width: 100%;'/><br><br><b>Thank you for your purchase!</b>"
                            +"<br><br><img src='cid:logo'/>";
                    helper.setText(content, true);

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status3.jpg".replace("\\", File.separator)));
                    helper.addInline("status3", resource);
                    break;
                case "delivering":
                    //Send email to recipient when their order is being delivered
                    helper.setSubject("Your order is being delivered!");
                    content = "<b>Dear customer</b>,<br>Your order is now on its way to reach you!"
                            +"<br>Please check your order status below.<br>When the order status has been updated, you will receive another email from us."
                            + "<br><img src='cid:status4' style='max-width: 100%;'/><br><br><b>Thank you for your purchase!</b>"
                            +"<br><br><img src='cid:logo'/>";
                    helper.setText(content, true);

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status4.jpg".replace("\\", File.separator)));
                    helper.addInline("status4", resource);
                    break;
                case "delivered":
                    //Send email to recipient when their order has been delivered
                    helper.setSubject("Your order has been delivered successfully!");
                    content = "<b>Dear customer</b>,<br>This email is to confirm that your order has been delivered successfully."
                            +"<br>Please check your order status below.<br>Our system will now mark your order as complete."
                            + "<br><img src='cid:status5' style='max-width: 100%;'/><br><br><b>Thank you for your purchase!</b>"
                            +"<br><b>We look forward to seeing you again!</b>"
                            +"<br><br><img src='cid:logo'/>";
                    helper.setText(content, true);

                    resource = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\status5.jpg".replace("\\", File.separator)));
                    helper.addInline("status5", resource);
                    break;
                case "cancelled":
                    //Send email to recipient when their order has been cancelled
                    break;*/
            }

            helper.setSubject(subject);
            thymeleafContext.setVariable("mailContent",content);
            String mailTemplate = emailTemplateEngine.process("email-template.html",thymeleafContext);
            helper.setText(mailTemplate, true);
            helper.addInline("status", resource);
            FileSystemResource logo = new FileSystemResource(new File("target\\classes\\static\\img\\logo.png".replace("\\", File.separator)));
            helper.addInline("logo", logo);
            FileSystemResource logo1 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\facebook.png".replace("\\", File.separator)));
            helper.addInline("facebook", logo1);
            FileSystemResource logo2 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\twitter.png".replace("\\", File.separator)));
            helper.addInline("twitter", logo2);
            FileSystemResource logo3 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\linkedin.png".replace("\\", File.separator)));
            helper.addInline("linkedin", logo3);
            FileSystemResource logo4 = new FileSystemResource(new File("target\\classes\\static\\img\\contact\\instagram.png".replace("\\", File.separator)));
            helper.addInline("instagram", logo4);
            FileSystemResource footer = new FileSystemResource(new File("target\\classes\\static\\img\\hero\\hero-1.jpg".replace("\\", File.separator)));
            helper.addInline("footer", footer);
            mailSender.send(message);

        } catch (MessagingException e){
            System.out.println("Cannot send email to "+receiver);
        }

    }

}
