package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private AccountService accountService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    // display registration form when path is accessed
    @RequestMapping(value="registration", method=RequestMethod.GET)
    public String getForm(ModelMap model){    // ModelMap is used to parse objects from controller to html through thymeleaf
        model.addAttribute("account", new Account());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //If user is already logged in, redirect to home page
            return "redirect:/";
        }
        return "registration";
    }

    // sign up new user after they fill out and submit the registration form
    @RequestMapping(value="registration", method=RequestMethod.POST)
    public String register(@ModelAttribute(value="account") Account account, Model model){
        try{
            model.addAttribute("account", account);
            account.setAccountRole(AccountRole.USER);
            accountService.signUpAccount(account);
        }
        catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return"registration";
        }
        return "redirect:/login";
    }

    // display default landing page after successful log in
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String getWelcomePage(ModelMap model){
        model.addAttribute("account", new Account());
        float cartSum = 0;
        int cartQty = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //If the user is already logged in, update their top-right cart info
            Account loggedInAcc = (Account)auth.getPrincipal();
            Long userId = loggedInAcc.getId();

            Account user = accountService.getAccountById(userId);
            List<Cart> cartList = cartService.getAllCarts(user);

            cartQty = cartList.size();
            for (Cart cart : cartList) {
                cartSum += cart.getSmallSum();
            }
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        return "index";
    }

    // display update account form when path is accessed
    @RequestMapping(value="user/update", method=RequestMethod.GET)
    public String getUpdateAccountForm(ModelMap model){
        // Get current account authorization and get that account
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        model.addAttribute("account", loggedInAcc);
        return "update_account";
    }

    // user update their account information (first name, last name, phone, email, address)
    @RequestMapping(value="user/update", method=RequestMethod.POST)
    public String update(@Valid Account account, Model model){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account loggedInAcc = (Account)auth.getPrincipal();
            Long userId = loggedInAcc.getId();
            account.setId(userId);
            accountService.updateAccount(userId, account);
        }
        catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return"update_account";
        }
        return "redirect:/logout" ;
    }

    // user update their password
    @RequestMapping(value="user/update/password", method=RequestMethod.POST)
    public String updatePassword(@Valid Account account){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        account.setId(userId);

        accountService.changePassword(userId, account.getPassword());
        return "redirect:/logout";
    }

    // user delete their account
    @RequestMapping(value="user/delete", method=RequestMethod.POST)
    public String deleteAccount(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        accountService.deleteAccount(userId);
        return "redirect:/logout";
    }

    // display account management system when path is accessed
    @RequestMapping(value="account-management", method=RequestMethod.GET)
    public String showAccountManagementSystem(ModelMap model){
        List<Account> accountList = accountService.getAllAccounts();
        accountList.sort(Comparator.comparing(Account::getId)); // sort account list by id number (low to high id number)
        model.addAttribute("accountList",accountList);
        return "account_list";
    }

    // admin make another user account an admin
    @RequestMapping(value="account-management/make-admin/{id}", method= RequestMethod.GET)
    public String makeAccountAdmin(@PathVariable Long id){
        accountService.setAccountRole(id, AccountRole.ADMIN);
        return "redirect:/account-management";
    }

    // admin revoke another admin's admin rights
    @RequestMapping(value="account-management/revoke-admin/{id}", method= RequestMethod.GET)
    public String revokeAccountAdmin(@PathVariable Long id){
        accountService.setAccountRole(id, AccountRole.USER);
        return "redirect:/account-management";
    }

    // user view their order history
    @RequestMapping(value="order-history", method = RequestMethod.GET)
    public String getOrderHistory(ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        float cartSum = 0;
        int cartQty = 0;
        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);

        cartQty = cartList.size();
        for (Cart cart : cartList) {
            cartSum += cart.getSmallSum();
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        model.addAttribute("orderList", orderService.getOrdersByAccountId(userId));
        return "order_history";
    }
}
