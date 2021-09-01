package com.example.ordersystem.controller;

import com.example.ordersystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/sendmail/{status}")
    public String sendEmail(@PathVariable String status) {
        emailService.sendEmail("jinesew933@mi166.com",status);
        return "Email sent successfully!";
    }
}
