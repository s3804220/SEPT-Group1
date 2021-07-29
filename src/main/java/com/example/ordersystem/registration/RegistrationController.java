package com.example.ordersystem.registration;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(path="registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @GetMapping
    public String getForm(){
        return "registration";
    }
    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
}
