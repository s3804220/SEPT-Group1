package com.example.ordersystem.controller;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.Pagination;
import com.example.ordersystem.service.AccountService;
import com.example.ordersystem.service.CartService;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CartService cartService;

    @GetMapping("/shop")
    public String listAll(ModelMap model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(name="sortField", defaultValue = "id") String sortField
//                          @RequestParam(defaultValue = "id", name="sortField") String sortField,
//                          @RequestParam(defaultValue = "asc", name="sortDir") String sortDir
    ) {
        System.out.println("SHOPHSOHPSHOPSHPOSHPS");
        String sortDir = "asc";

        // The number of total items
        int totalNum = itemService.findTotal();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize, sortField, sortDir);

        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDirection", sortDir);
//        model.addAttribute("reverseSortDirection", sortDir.equals("asc") ? "desc" : "asc");


        model.addAttribute("shopList", shopList);
        model.addAttribute("pagination", pagination);

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
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);

        return "shop";
    }

    @GetMapping("/shop-details")
    public String readDetail(ModelMap model, @RequestParam("id") Long id) throws Exception {

        model.addAttribute("shopDetail", itemService.getItem(id).get());

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
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);

        return "shop-details";
    }

    @GetMapping(path = "/item-form")
    public String itemForm(ModelMap model){
        float cartSum = 0;
        int cartQty = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);

        cartQty = cartList.size();
        for (Cart cart : cartList) {
            cartSum += cart.getSmallSum();
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        return "item-form";
    }

    @GetMapping(path = "/item-list")
    public String itemList(ModelMap model){
        List<Item> itemList = itemService.getAllItemsSortedId();
        model.addAttribute("itemList",itemList);
        float cartSum = 0;
        int cartQty = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);

        cartQty = cartList.size();
        for (Cart cart : cartList) {
            cartSum += cart.getSmallSum();
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);
        return "item-list";
    }

    @GetMapping(path = "/items/setavailability/{id}")
    public String changeAvailability(@PathVariable Long id){
        itemService.changeAvailability(id);
        return "redirect:/item-list";
    }


//    @PostMapping("/sort")
//    public String addItemToCart(Model model,
//                                @RequestParam("shopId") Long itemId,
//                                @RequestParam("amount") int amount) {
//
//        model.addAttribute("sortBy", )
//
//        return "redirect:/shopping-cart";
//    }
    @GetMapping("/shop/delete/{deleteId}")
    public String delete(@PathVariable(name = "deleteId") Long id) {
        cartService.deleteCart(id);
        return "redirect:/shop";
    }
}
