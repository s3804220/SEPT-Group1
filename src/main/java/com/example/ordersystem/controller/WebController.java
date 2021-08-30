package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    private UnifiedService unifiedService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/table")
    public String table(Model model) {
        var words = List.of("mountain", "noon", "rock", "river", "spring");
        model.addAttribute("words", words);
        return "table";
    }

    @GetMapping("/admin-panel")
    public String adminPanel(ModelMap model){
        unifiedService.getCartInfo(model);
        return "admin-panel";
    }

    @GetMapping("/about")
    public String aboutPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "contact";
    }

    @GetMapping("/access-denied")
    public String accessPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "access-denied";
    }
}