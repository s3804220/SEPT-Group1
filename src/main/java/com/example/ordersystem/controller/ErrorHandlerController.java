package com.example.ordersystem.controller;

import com.example.ordersystem.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlerController implements ErrorController {
    @Autowired
    private UnifiedService unifiedService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "redirect:/not-found";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error500";
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                return "error400";
            }
        }
        return "error";
    }

    @RequestMapping("/not-found")
    public String notFoundPage(ModelMap model){
        unifiedService.getCartInfo(model);
        return "not-found";
    }
}
