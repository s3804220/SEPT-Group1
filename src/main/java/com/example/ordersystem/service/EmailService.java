package com.example.ordersystem.service;

import com.example.ordersystem.model.Email;
import com.example.ordersystem.model.EmailFactory;
import com.example.ordersystem.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Transactional
@Service
public class EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine emailTemplateEngine;

    public void sendEmail(String status, Order order){
        String sender = "sept.system1@gmail.com";

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(order.getAccount().getEmail());

            Context thymeleafContext = new Context();

            Email newEmail = EmailFactory.writeEmail(status);

            //Set the subject of the email to be sent
            helper.setSubject(newEmail.getSubject());
            thymeleafContext.setVariable("orderstatus",newEmail.getOrderStatus());
            thymeleafContext.setVariable("mailContent",newEmail.getEmailContent());
            thymeleafContext.setVariable("orderID",order.getId());
            thymeleafContext.setVariable("orderTotal",order.getPrice());

            String mailTemplate = emailTemplateEngine.process("email-template.html",thymeleafContext);
            helper.setText(mailTemplate, true);

            helper.addInline("logo", newEmail.getResources().get(0));
            helper.addInline("facebook", newEmail.getResources().get(1));
            helper.addInline("twitter", newEmail.getResources().get(2));
            helper.addInline("instagram", newEmail.getResources().get(3));
            helper.addInline("footer", newEmail.getResources().get(4));
            helper.addInline("status", newEmail.getResources().get(5));

            mailSender.send(message);

        } catch (MessagingException e){
            System.out.println("There was an error when sending the email!\n");
            e.printStackTrace();
        }

    }

}
