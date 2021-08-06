package com.example.ordersystem.controller;

import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Pagination;
import com.example.ordersystem.model.Shop;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.ShopService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    private CartService cartService;
    private ShopService shopService;


    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }



    @GetMapping("/shoping-cart/list")
    public String readDetail(Model model) throws Exception {
        System.out.println("HelloTHerererere@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

//        System.out.println("@@@@@@@@@@@@ shop2 has these: ");

//
        cartService.addCart(new Cart(1L, "uId1", "user1", 1L, "cupcake1st",
                new BigDecimal("31.00"),"product-1.jpg", 1));

        cartService.addCart(new Cart(2L, "uId2", "user2", 2L, "cupcake2",
                new BigDecimal("32.00"), "product-2.jpg", 2));

        List<Cart> cartList = cartService.getAllCarts();

        model.addAttribute("shopingCart", cartList);
//        model.addAttribute("shopDetail", shopService.getShop(id));

        return "shoping-cart";
    }


//    @PostMapping("/shoping-cart/add/{sid}/{samount}")
//    public String addShopToCart(@PathVariable("sid") Long shopId,
//                                @PathVariable("samount") int amount) throws NotFoundException{
//        String userId = "uId1";
//        if(userId == null) {
//            throw new NotFoundException("Not found any user with ID: " + userId);
//        }
//
//        int addedAmount = cartService.addCart(shopId, amount, userId);
//
//        return addedAmount + " item(s) are added to cart!!!";
//    }


    @PostMapping("/shoping-cart/insert")
    public String addShopToCart(
            @ModelAttribute("cart") Cart cart,
                                HttpSession session,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam int amount, //(value = "amount", required = false)
                                Model model) throws NotFoundException {
//        String userId = (String) session.getAttribute("userId");
        String userId = "uId1";
        if(userId == null) {
            throw new NotFoundException("Not found any user with ID: " + userId);
        }
        System.out.println("@@@@@@@@@@@이건 카운트: " + amount + " 그리고 ID: " + id);
//        cart.setUserId(userId);
//
//
        // Check if cart is not empty
        int count = cartService.countCart(cart.getShopId(), userId);
        System.out.println("@@@@@@@@@@@이건 카운트: " + count);
        if (count == 0) {
            cartService.addCart(cart);
        } else {
            cartService.updateCart(cart);
        }

        return "shoping-cart/list";
    }

    @DeleteMapping("/shoping-cart/{id}")
    public String delete(@RequestParam Long id) {
        cartService.deleteCart(id);
        return "shoping-cart";
    }

    @PutMapping("/shoping-cart/update/{sid}/{amount}")
    public String update(@RequestParam(name = "shopId") Long[] shopId,
                         @RequestParam(name = "amount") int[] amount, HttpSession session) {
//        String userId = (String) session.getAttribute("userId");
        System.out.println("@@@@@@@@ update clicked!");
        String userId = "uId1";
        for (int i=0; i<shopId.length; i++) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setAmount(amount[i]);
            cart.setShopId(shopId[i]);
            cartService.modifyCart(cart);
        }
        return "shoping-cart/list";
    }
}