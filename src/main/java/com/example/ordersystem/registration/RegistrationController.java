package com.example.ordersystem.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(path="registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @GetMapping()
    public String getForm(){
        return "registration";
    }

    @PostMapping
    public void register(@RequestBody RegistrationRequest request){
        registrationService.register(request);
    }
}
