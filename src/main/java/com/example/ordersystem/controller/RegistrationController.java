package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Student;
import com.example.ordersystem.repository.AccountRepository;
import com.example.ordersystem.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
//@RequestMapping(path="registration")
@AllArgsConstructor
public class RegistrationController {

    private AccountService accountService;

    @RequestMapping(value="registration")
    public String getForm(ModelMap model){
        model.addAttribute("account", new Account());
        return "registration";
    }

    @RequestMapping(value="registration", method=RequestMethod.POST)
    public String register(@ModelAttribute(value="account") Account account, Model model){
        model.addAttribute("account", account);
        account.setAccountRole(AccountRole.USER);
        accountService.signUpAccount(account);
        return "redirect:/login";
    }
}
