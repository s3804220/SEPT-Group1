package com.example.ordersystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Order;
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
    
    @RequestMapping(value="/checkout/orderlist", method=RequestMethod.GET)
    public String showOrderList(ModelMap model){
        List<Order> orderList = orderService.getAllOrders();
        model.addAttribute("orderList",orderList);
        return "orderlist";
    }
    
    @RequestMapping(value="checkout/orderlist/confirm-order/{id}", method= RequestMethod.GET)
    public String confirmorder(@PathVariable Long id){
    	Account user = accountService.getAccountById(id);
        orderService.confirmOrder(user);
        return "redirect:/checkout/orderlist";
    }

    @RequestMapping(value="checkout/orderlist/unconfirm-order/{id}", method= RequestMethod.GET)
    public String unconfirmOrder(@PathVariable Long id){
    	Account user = accountService.getAccountById(id);
        orderService.unconfirmOrder(user);
        return "redirect:/checkout/orderlist";
    }
}
