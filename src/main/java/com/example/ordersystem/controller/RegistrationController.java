package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class is used for routing endpoints to perform CRUD on accounts and other account functionalities,
 * such as user view their order history and admin account management system
 */

@Controller
@AllArgsConstructor
public class RegistrationController {
    private AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UnifiedService unifiedService;
    @Autowired
    private ItemService itemService;
    
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Mapping to display registration form when path is accessed
     * @param model - The ModelMap is used to parse objects from controller to HTML using Thymeleaf
     * @return A String which is the HTML registration form template
     */
    @RequestMapping(value="registration", method=RequestMethod.GET)
    public String getForm(ModelMap model){
        model.addAttribute("account", new Account());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //If user is already logged in, redirect to home page
            return "redirect:/";
        }
        return "registration";
    }

    /**
     * Mapping to sign up new user after they fill out and submit the registration form
     * @param account - The new Account object to be signed up
     * @param model - The model which contains the error message (if any) to be shown in frontend via Thymeleaf
     * @return On success, show the login form for user to login after registration. On failure, stay on registration page and show error.
     */
    @RequestMapping(value="registration", method=RequestMethod.POST)
    public String register(@ModelAttribute(value="account") Account account, Model model){
        try{ // try signing up a new account
            model.addAttribute("account", account);
            account.setAccountRole(AccountRole.USER);
            accountService.signUpAccount(account);
        }
        catch(Exception e){ // if any exceptions are thrown, then show it in frontend
            model.addAttribute("errorMessage", e.getMessage());
            return"registration";
        }
        return "redirect:/login";
    }

    /**
     * Mapping to display default landing page (which is the homepage)
     * @param model - The ModelMap that contains user information to be displayed on frontend via Thymeleaf
     * @return The homepage
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String getWelcomePage(ModelMap model){
        model.addAttribute("account", new Account());
        unifiedService.getCartInfo(model);
        return "index";
    }

    /**
     * Mapping to display the update account form when path is accessed
     * @param model - The ModelMap is used to parse objects from controller to HTML using Thymeleaf
     * @return The update account form
     */
    @RequestMapping(value="user/update", method=RequestMethod.GET)
    public String getUpdateAccountForm(ModelMap model){
        // Get current account authorization and get that account
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        model.addAttribute("account", loggedInAcc);
        return "update_account";
    }

    /**
     * Mapping to send the user's updated account information (first name, last name, phone, email, address) to backend
     * @param account - The user Account to be updated
     * @param model - The model which contains the error message (if any) to be shown in frontend via Thymeleaf
     * @return On success, log the user out and show the login form. On failure, stay on the update account page and show error.
     */
    @RequestMapping(value="user/update", method=RequestMethod.POST)
    public String update(@Valid Account account, Model model){
        try{ // try updating account
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account loggedInAcc = (Account)auth.getPrincipal();
            Long userId = loggedInAcc.getId();
            account.setId(userId);
            accountService.updateAccount(userId, account);
        }
        catch(Exception e){ // if any exceptions are thrown, then show it in fronted
            model.addAttribute("errorMessage", e.getMessage());
            return"update_account";
        }
        return "redirect:/logout" ;
    }

    /**
     * Mapping to let user update their password
     * @param account - The user Account to be updated
     * @return Log the user out and show the login form
     */
    @RequestMapping(value="user/update/password", method=RequestMethod.POST)
    public String updatePassword(@Valid Account account){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        account.setId(userId);

        accountService.changePassword(userId, account.getPassword());
        return "redirect:/logout";
    }

    /**
     * Mapping to let users delete their account
     * @return Log out and redirect the user to the login page
     */
    @RequestMapping(value="user/delete", method=RequestMethod.POST)
    public String deleteAccount(){
        //Get the user's currently logged in account
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        //Delete that account
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
        model.addAttribute("orderList", orderService.getOrdersByAccountId(userId));
        unifiedService.getCartInfo(model);
        return "order_history";
    }

    // user view their order's details from order history
    @RequestMapping(value="order-history/order-details/{id}", method = RequestMethod.GET)
    public String viewOrderDetailsFromOrderHistory(@PathVariable Long id, ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        List<Order> accOrders = orderService.getOrdersByAccountId(loggedInAcc.getId());
        for (Order order : accOrders){
            if (order.getId().equals(id)){
                String itemString = order.getItems();
                itemString = itemString.substring(1, itemString.length() - 1);
                String[] items = itemString.split("\\Q},{\\E");
                ArrayList<String[]> itemInfo = new ArrayList<>();
                for(String item: items) {
                    String[] iteminfo = item.split(",");
                    iteminfo[0] = itemService.getItem(Long.valueOf(iteminfo[0])).get().getItemName();
                    itemInfo.add(iteminfo);
                }
                model.addAttribute("itemInfo",itemInfo);
                model.addAttribute("order", order);
                return "order_history_view_order_details";
            }
        }
        return "access-denied";
    }
}
