package com.example.ordersystem.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Message;

import static org.junit.jupiter.api.Assertions.*;

//Since Gmail service cannot be unit tested separately, Greenmail is used as a local email service for testing
@ExtendWith(SpringExtension.class)
@ContextConfiguration("ApplicationContext-Greenmail.xml")
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmailTest() {
        //Create new Greenmail instance to test the email service
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
        try {
            greenMail.start();
            //Call email service to send an email for order status confirmation
            emailService.sendEmail("example@example.com","created");
            emailService.sendEmail("example2@example.com","confirmed");
            assertTrue(greenMail.waitForIncomingEmail(5000, 2));
            //Retrieve emails using GreenMail API
            Message[] messages = greenMail.getReceivedMessages();
            String subject1 = messages[0].getSubject();
            String subject2 = messages[1].getSubject();
            //Assert that two emails were sent
            assertEquals(2, messages.length);
            //Assert that the subjects match what is expected
            assertEquals("Your order has been created!", subject1);
            assertEquals("Your order has been confirmed!", subject2);
            //Assert that the recipients of the emails are correct
            assertEquals("example@example.com", messages[0].getAllRecipients()[0].toString());
            assertEquals("example2@example.com", messages[1].getAllRecipients()[0].toString());
            //Assert that the sender address is correct
            assertEquals("sept.system1@gmail.com",messages[0].getFrom()[0].toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            greenMail.stop();
        }
    }
}