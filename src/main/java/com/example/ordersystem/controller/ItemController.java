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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
                          @RequestParam(name="sortField", defaultValue = "id") String sortField,
                          @RequestParam(name="filterField", defaultValue = "All") String filterField ,
                          @RequestParam(name="search-input", defaultValue = "") String searchField
    ) {

        // The number of total items
        int totalNum = itemService.getAllItems().size();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        // Check if value of sortFild is valid
        String[] validSort = new String[]{"id","name","priceHTL","priceLTH"};
        if(!Arrays.asList(validSort).contains(sortField)){
            sortField = "id";
        }


        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize, filterField, sortField, searchField);
        List<Item> fullItemList = itemService.getAllItems();

        // Get pagination when Filter is used
        pagination = new Pagination(itemService.findNumOfFilteredItems(filterField)+1, page);
        System.out.println("@@@@@@ itemService.findNumOfFilteredItems(filterField): "+itemService.findNumOfFilteredItems(filterField));
        System.out.println("@@@@@@ pagination.getTotalPages: "+pagination.getTotalPages());

        // Get a list of categories of items
        List<String> categoryListwithDuplicates = new ArrayList<>();
        categoryListwithDuplicates.add("All");


        for (Item item : fullItemList) {
            categoryListwithDuplicates.add(item.getCategory());
        }

        List<String> categoryList = new ArrayList<String>(new LinkedHashSet<>(categoryListwithDuplicates));

        // Check if value of filterFild is valid
        if(!categoryList.contains(filterField)){
            filterField = "id";
        }


        model.addAttribute("filterField", filterField);
        model.addAttribute("sortField", sortField);
        model.addAttribute("shopList", shopList);
        model.addAttribute("categoryList", categoryList);
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




}
