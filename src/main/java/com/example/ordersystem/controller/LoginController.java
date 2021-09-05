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

    /**
     * Mapping for the path to the login page
     * @param request - The HTTP request which leads to the login page
     * @param model - A ModelMap which contains information to send to the frontend template via Thymeleaf
     * @return A String which is the login form template
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, ModelMap model){
        //Store the URL that the user visited before logging in
        // to redirect them back to it after the login is successful
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

    /**
     * Mapping for the path to handle the logout request
     * @param request - The HTTP request to logout
     * @param response - The HTTP response to log the user out of their session
     * @return A String which is the login form template, showing that the user has been logged out
     */
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
