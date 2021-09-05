package com.example.ordersystem.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class defines the custom success handler for when the user successfully login
 * After login, it will redirect the user to the page they were previously browsing
 */
public class MyCustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public MyCustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            //Get the previous URL attribute to get ready for redirect
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null && !redirectUrl.contains("user/update")) {
                // Clean the previous URL attribute from session
                session.removeAttribute("url_prior_login");
                // then redirect back to the previous page
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else if (redirectUrl != null) {
                session.removeAttribute("url_prior_login");
                super.onAuthenticationSuccess(request, response, authentication);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
