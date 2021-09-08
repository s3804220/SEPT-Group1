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

import java.util.Iterator;
import java.util.List;

/**
 * This class is the service class which mostly combines other services in its methods
 */
@Transactional
@Service
public class UnifiedService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CartService cartService;

    /**
     * Method to get the current cart information for the currently logged in user
     * @param model - The ModelMap which contains the information to pass to the frontend through Thymeleaf
     */
    public void getCartInfo(ModelMap model){
        float cartSum = 0;
        int cartQty = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            //If the user is already logged in, update the cart icon at the top-right to show the correct information
            Account loggedInAcc = (Account)auth.getPrincipal();
            Long userId = loggedInAcc.getId();

            Account user = accountService.getAccountById(userId);
            //Get list of all carts belonging to the current user
            List<Cart> cartList = cartService.getAllCarts(user);
            //Create an iterator to iterate through the cart
            Iterator<Cart> cartIterator = cartList.listIterator();

            //Set the cart quantity equal to the number of unique items in the cart
            cartQty = cartList.size();
            while (cartIterator.hasNext()){
                //Get the subtotal price of each item
                cartSum += cartIterator.next().getSmallSum();
            }
        }
        //Send the information as a model to the template for display
        model.addAttribute("cartQty",cartQty);
        model.addAttribute("cartSum",cartSum);
    }
}
