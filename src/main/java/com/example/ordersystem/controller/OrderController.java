package com.example.ordersystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.OrderService;

@Controller
public class OrderController {
	
	private OrderService orderService;
	private AccountService accountService;
	private CartService cartService;
	
	@Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
	
	@Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
	
    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/checkout")
    public String checkout(ModelMap model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);
        model.addAttribute("cartItems", cartList);

        return "checkout";
    }
    
    @PostMapping("/checkout/add")
    public String addShopToCart() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);
        orderService.addOrder(user);

        return "redirect:/shoping-cart";
    }
}
