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

    //Map all errors to different paths based on the HTTP status code
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

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

    //Map the template for 404 Not Found error
    @RequestMapping("/not-found")
    public String notFoundPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "not-found";
    }

    //Map the template for 500 Internal Server error
    @RequestMapping("/internal-server-error")
    public String InternalErrorPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "internal-server-error";
    }

    //Map the template for 400 Bad Request error
    @RequestMapping("/bad-request")
    public String badRequestPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "bad-request";
    }

    //Map the template for 403 Access Denied error
    @GetMapping("/access-denied")
    public String accessPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "access-denied";
    }
}
