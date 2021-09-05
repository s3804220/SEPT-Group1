package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    private CartService cartService;
    private ItemService itemService;
    private AccountService accountService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

// List items in shopping cart
    @GetMapping("/shopping-cart")
    public String readDetail(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);
        float cartSum = 0;
        int cartQty = cartList.size();
        for (Cart cart : cartList) {
            cartSum += cart.getSmallSum();
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        model.addAttribute("cartItems", cartList);

        return "shopping-cart";
    }

    @PostMapping("/shopping-cart/add")
    public String addItemToCart(@RequestParam("shopId") Long itemId,
                                @RequestParam("amount") int amount) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);

        Item itemToAdd = itemService.getItem(itemId).get();
        if(itemToAdd.isAvailability()){
            int addedAmount = cartService.addItem(itemId, amount, user);
            return "redirect:/shopping-cart";
        }else{
            return "redirect:/shop-details?id="+itemId;
        }
    }

    @GetMapping("/shopping-cart/delete/{deleteId}")
    public String delete(@PathVariable(name = "deleteId") Long id) {
        cartService.deleteCart(id);
        return "redirect:/shopping-cart";
    }

// Update amount of an item in cart
    @PostMapping("/shopping-cart/update")
    public String update(@RequestParam(name = "shopId") Long itemId,
                         @RequestParam(name = "amount") int amount) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);

        int smallSum = cartService.updateAmount(amount, itemId, user);

        return "redirect:/shopping-cart";
    }
}