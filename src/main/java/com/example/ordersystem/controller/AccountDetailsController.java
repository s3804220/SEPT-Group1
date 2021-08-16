package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class AccountDetailsController {
    private AccountService accountService;
    private OrderService orderService;

    @RequestMapping(value="admin/account-management/account-details/{id}", method= RequestMethod.GET)
    public String viewUserDetails(@PathVariable Long id, ModelMap model){
        Account accountToView = accountService.getAccountById(id);
        model.addAttribute("account", accountToView);
        return "account-details";
    }

    @RequestMapping(value="admin/account-management/order-history/{id}", method = RequestMethod.GET)
    public String viewUserOrderHistory(@PathVariable Long id, ModelMap model){
        model.addAttribute("orderList", orderService.getOrdersByAccountId(id));
        return "admin-user-order-history";
    }
}
