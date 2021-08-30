package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.List;

@Transactional
@Service
public class UnifiedService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CartService cartService;

    public void getCartInfo(ModelMap model){
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
        model.addAttribute("cartQty",cartQty);
        model.addAttribute("cartSum",cartSum);
    }
}
