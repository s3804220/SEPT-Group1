package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartRestController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AccountService accountService;

//    @PostMapping("/shoping-cart/add")
//    public String addShopToCart(@RequestParam("sid") Long shopId,
//                          @RequestParam("amount") int amount) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
//            return "You must login to continue";
//        }
//
//        Account loggedInAcc = (Account)auth.getPrincipal();
//        Long userId = loggedInAcc.getId();
//        Account user = accountService.getAccountById(userId);
//
//        int addedAmount = cartService.addShop(shopId, amount, user);
//
//        System.out.println(amount+" items added!");
//        return "/shoping-cart";
//    }

}
