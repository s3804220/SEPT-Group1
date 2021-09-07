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

/**
 * This class is the service layer for the email system
 * It provides a method that can be called to send an email for an order with a specific status
 */
@Transactional
@Service
public class EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine emailTemplateEngine;

    /**
     * Method to send an email with the order status to the user email who owns that order
     * @param status - The order status to be sent
     * @param order - The Order object for which the email will be generated
     */
    public void sendEmail(String status, Order order){
        String sender = "sept.system1@gmail.com";

        try{
            //Create and configure new empty MimeMessage to be sent
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            //Set the sender and receiver of the email
            helper.setFrom(sender);
            helper.setTo(order.getAccount().getEmail());

            //Create new thymeleaf context, which later can be filled with variables and sent to the email template
            Context thymeleafContext = new Context();

            //Call email factory to provide a new email object based on the order status
            Email newEmail = EmailFactory.writeEmail(status);

            //Set the subject of the email to be sent
            helper.setSubject(newEmail.getSubject());
            //Set the variables using thymeleaf, which will be passed to the email template
            thymeleafContext.setVariable("orderstatus",newEmail.getOrderStatus());
            thymeleafContext.setVariable("mailContent",newEmail.getEmailContent());
            thymeleafContext.setVariable("orderID",order.getId());
            thymeleafContext.setVariable("orderTotal",order.getPrice());

            //Process the variables and put them into the email template
            String mailTemplate = emailTemplateEngine.process("email-template.html",thymeleafContext);
            helper.setText(mailTemplate, true);

            //Add image resources to display in the email template
            helper.addInline("logo", newEmail.getResources().get(0));
            helper.addInline("facebook", newEmail.getResources().get(1));
            helper.addInline("twitter", newEmail.getResources().get(2));
            helper.addInline("instagram", newEmail.getResources().get(3));
            helper.addInline("footer", newEmail.getResources().get(4));
            helper.addInline("status", newEmail.getResources().get(5));

            //Finally, send the email
            mailSender.send(message);

        } catch (MessagingException e){
            System.out.println("There was an error when sending the email!\n");
            e.printStackTrace();
        }

    }

}
