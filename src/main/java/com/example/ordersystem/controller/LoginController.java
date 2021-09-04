package com.example.ordersystem.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is used for routing handlers for the login and logout pages.
 */
@Controller
public class LoginController {

    //Map the path for the login page and return the correct template
    @GetMapping("/login")
    public String login(HttpServletRequest request, ModelMap model){
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        float cartSum = 0;
        int cartQty = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //If the user is already logged in, redirect to the home page instead
            return "redirect:/";
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        return "login";
    }

    //Map the path for the logout request
    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Logout of the session if the user is currently logged in
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
