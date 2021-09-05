package com.example.ordersystem.controller;

import com.example.ordersystem.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is used for routing general pages like the admin panel, about and contact pages.
 */
@Controller
public class WebController {
    @Autowired
    private UnifiedService unifiedService;

    //Map the path for Admins to access the admin panel
    @GetMapping("/admin-panel")
    public String adminPanel(ModelMap model){
        unifiedService.getCartInfo(model);
        return "admin-panel";
    }

    //Map the path to the about page
    @GetMapping("/about")
    public String aboutPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "about";
    }

    //Map the path to the contact page
    @GetMapping("/contact")
    public String contactPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "contact";
    }
}