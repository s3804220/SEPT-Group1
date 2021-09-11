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

    /**
     * Mapping to show the admin panel for Admins only when the path is accessed
     * @param model - The ModelMap which contains information to be sent to frontend via Thymeleaf
     * @return The Admin panel page
     */
    @GetMapping("/admin-panel")
    public String adminPanel(ModelMap model){
        unifiedService.getCartInfo(model);
        return "admin-panel";
    }

    /**
     * Mapping to show the about page when the path is accessed
     * @param model - The ModelMap which contains information to be sent to frontend via Thymeleaf
     * @return The About page
     */
    @GetMapping("/about")
    public String aboutPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "about";
    }

    /**
     * Mapping to show the contact page when the path is accessed
     * @param model - The ModelMap which contains information to be sent to frontend via Thymeleaf
     * @return The Contact page
     */
    @GetMapping("/contact")
    public String contactPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "contact";
    }
}