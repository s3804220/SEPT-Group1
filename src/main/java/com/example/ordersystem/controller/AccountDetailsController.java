package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class AccountDetailsController {
    private AccountService accountService;
    private OrderService orderService;

    // Admin view specific user's account details
    @RequestMapping(value="account-management/account-details/{id}", method= RequestMethod.GET)
    public String viewUserDetails(@PathVariable Long id, ModelMap model){
        Account accountToView = accountService.getAccountById(id);
        model.addAttribute("account", accountToView);
        return "account-details";
    }

    // Admin view specific user's history
    @RequestMapping(value="account-management/order-history/{id}", method = RequestMethod.GET)
    public String viewUserOrderHistory(@PathVariable Long id, ModelMap model){
        List<Order> userOrdersList = orderService.getOrdersByAccountId(id);
        userOrdersList.sort(Comparator.comparing(Order::getId)); // sort order by id number(low to high id number)
        model.addAttribute("orderList", userOrdersList);
        return "admin-user-order-history";
    }
}
