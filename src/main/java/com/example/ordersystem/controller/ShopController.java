package com.example.ordersystem.controller;

import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Pagination;
import com.example.ordersystem.model.Shop;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class ShopController {

    private ShopService shopService;
    private CartService cartService;


    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }


    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }

//    @GetMapping("/greeting")
//    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
//        return "greeting";
//    }


    @GetMapping("/shop")
    public String listAll(Model model, @RequestParam(defaultValue = "1") int page) {

        // The number of total items
        int totalNum = shopService.findTotal();

        Pagination pagination = new Pagination(totalNum, page);

        int startIndex = pagination.getStartIndex();
        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        List<Shop> shopList = shopService.findListPaging(startIndex, pageSize);



        shopService.saveShop(new Shop(1L, "cupcake1st", new BigDecimal("32.00"), "Good day", "product-1.jpg"));
        shopService.saveShop(new Shop(2L, "cupcake2", new BigDecimal("31.00"),"Good night", "product-2.jpg"));
        shopService.saveShop(new Shop(3L, "cupcake3", new BigDecimal("31.00"), "", "product-3.jpg"));
        shopService.saveShop(new Shop(4L, "cupcake1", new BigDecimal("33.00"), "", "product-4.jpg"));
        shopService.saveShop(new Shop(5L, "cupcake2", new BigDecimal("33.00"), "", "product-5.jpg"));
        shopService.saveShop(new Shop(6L, "cupcake3", new BigDecimal("324.00"), "", ""));
        shopService.saveShop(new Shop(7L, "cupcake1", new BigDecimal("352.00"), "", ""));
        shopService.saveShop(new Shop(8L, "cupcake1", new BigDecimal("326.00"), "", ""));
        shopService.saveShop(new Shop(9L, "cupcake1", new BigDecimal("327.00"), "", ""));


        model.addAttribute("shopList", shopList);
        model.addAttribute("pagination", pagination);

        return "shop";
    }


//    @GetMapping("/shop")
//    public String listAll(Model model) throws Exception {
//
//        shopService.saveShop(shop1);
//
//        model.addAttribute("shops", shopService.getAllShops());
//        return "shop";
//    }

    @GetMapping("/shop-details")
    public String readDetail(Model model, @RequestParam("id") Long id) throws Exception {


        model.addAttribute("shopDetail", shopService.findShopById(id));
//        model.addAttribute("shopDetail", shopService.getShop(id));


        return "shop-details";
    }

//    @PostMapping("/shoping-cart")
//    public String editShopForCart(@PathVariable Long itemId, @ModelAttribute Cart cart) {
//
//
//        return "shoping-cart";
//    }
}