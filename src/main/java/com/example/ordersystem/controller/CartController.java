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

import java.util.List;

/**
 * This class is used for routing cart items and CRUD from the list of cart items
 */

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

    /**
     * Mapping to view all items currently in the shopping cart
     * @param model - The ModelMap to process and send values to the frontend template via Thymeleaf
     * @return A String which is the processed template
     */
    @GetMapping("/shopping-cart")
    public String readDetail(ModelMap model) {
        //Get the user account to update their shopping cart
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

    /**
     * Mapping to add an item to the shopping cart
     * @param itemId - The ID of the item to add
     * @param amount - The amount of the item to add
     * @return A String which is the processed shopping cart template
     */
    @PostMapping("/shopping-cart/add")
    public String addItemToCart(@RequestParam("shopId") Long itemId,
                                @RequestParam("amount") int amount) {

        //Get the user account to update their shopping cart
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);

        Item itemToAdd = itemService.getItem(itemId).get();

        //Check if the item is available, if it is, add it to the cart
        // if not, redirect back to the item details page with the appropriate message
        if(itemToAdd.isAvailability()){
            int addedAmount = cartService.addItem(itemId, amount, user);
            return "redirect:/shopping-cart";
        }else{
            return "redirect:/shop-details?id="+itemId;
        }
    }

    /**
     * Mapping to delete an item from the shopping cart
     * @param id - The ID of the item to delete
     * @return A String which is the processed shopping cart template
     */
    @GetMapping("/shopping-cart/delete/{deleteId}")
    public String delete(@PathVariable(name = "deleteId") Long id) {
        cartService.deleteCart(id);
        return "redirect:/shopping-cart";
    }

    /**
     * Mapping to update the quantity and subtotal price of a specific item in the cart
     * @param itemId - The ID of the item to update
     * @param amount - The quantity of the item to update
     * @return A String which is the processed shopping cart template
     */
    @PostMapping("/shopping-cart/update")
    public String update(@RequestParam(name = "shopId") Long itemId,
                         @RequestParam(name = "amount") int amount) {

        //Get the user account to update their shopping cart
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);

        //Call method to update the quantity according to the amount provided, and the subtotal price of that item
        int smallSum = cartService.updateAmount(amount, itemId, user);

        return "redirect:/shopping-cart";
    }
}