package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Pagination;
import com.example.ordersystem.model.Shop;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.ShopService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    private CartService cartService;
    private ShopService shopService;
    private AccountService accountService;


    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }



// List items in shoping cart
    @GetMapping("/shoping-cart")
    public String readDetail(ModelMap model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);
        System.out.println("shoping-cart list @@@");
//        model.addAttribute("shopingCart", cartList);
        model.addAttribute("cartItems", cartList);
    //        model.addAttribute("shopDetail", shopService.getShop(id));

        return "shoping-cart";
    }


    @PostMapping("/shoping-cart/add")
    public String addShopToCart(@RequestParam("sid") Long shopId,
                                @RequestParam("amount") int amount) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);


        int addedAmount = cartService.addShop(shopId, amount, user);

        System.out.println(amount+" items added!");
        return "redirect:/shoping-cart";
    }


    @GetMapping("/shoping-cart/delete/{deleteId}")
    public String delete(@PathVariable(name = "deleteId") Long id) {
        cartService.deleteCart(id);
        System.out.println("An Cart Deleted @@@@@@");
        return "redirect:/shoping-cart";
    }


// Update amount of an item in cart
    @PostMapping("/shoping-cart/update") ///{sid}/{amount}
    public String update(@RequestParam(name = "shopId") Long shopId,
                         @RequestParam(name = "amount") int amount, HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);

//        String userId = (String) session.getAttribute("userId");
        System.out.println("@@@@@@@@ update clicked!");
        System.out.println("shopName: "+ shopService.findShopById(shopId).getName());
        System.out.println("shopId: "+ shopId);
        System.out.println("amount: " + amount);
        System.out.println();

        int smallSum = cartService.updateAmount(amount, shopId, user);

        return "redirect:/shoping-cart";
    }
}