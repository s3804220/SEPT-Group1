package com.example.ordersystem.controller;

import com.example.ordersystem.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is used for routing error paths to specific handlers and templates.
 */
@Controller
public class ErrorHandlerController implements ErrorController {
    @Autowired
    private UnifiedService unifiedService;

    /**
     * Mapping to handle all errors and redirect to different templates based on the HTTP status code
     * @param request - The HTTP request which generates the error
     * @return A String which is the error template
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        //Check and get the HTTP error code (if any) from the request
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            //Redirect different errors to their respective templates
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "redirect:/not-found";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "redirect:/internal-server-error";
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                return "redirect:/bad-request";
            }
        }
        return "error";
    }

    /**
     * Mapping for the HTTP 404 Not Found error
     * @param model - The ModelMap which contains user information to send to the template via Thymeleaf
     * @return A String which is the processed error template
     */
    @RequestMapping("/not-found")
    public String notFoundPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "not-found";
    }

    /**
     * Mapping for the HTTP 500 Internal Server error
     * @param model - The ModelMap which contains user information to send to the template via Thymeleaf
     * @return A String which is the processed error template
     */
    @RequestMapping("/internal-server-error")
    public String InternalErrorPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "internal-server-error";
    }

    /**
     * Mapping for the HTTP 400 Bad Request error
     * @param model - The ModelMap which contains user information to send to the template via Thymeleaf
     * @return A String which is the processed error template
     */
    @RequestMapping("/bad-request")
    public String badRequestPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "bad-request";
    }

    /**
     * Mapping for the HTTP 403 Access Denied error
     * @param model - The ModelMap which contains user information to send to the template via Thymeleaf
     * @return A String which is the processed error template
     */
    @GetMapping("/access-denied")
    public String accessPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "access-denied";
    }
}
