package com.example.ordersystem.controller;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.example.ordersystem.service.*;
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

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Order;

@Controller
public class OrderController {
	
	private OrderService orderService;
	private AccountService accountService;
	private CartService cartService;
	private ItemService itemService;
	private UnifiedService unifiedService;
	
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
    
    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setUnifiedService(UnifiedService unifiedService){
	    this.unifiedService = unifiedService;
    }
    
    /**
     * Mapping to display checkout form when path is accessed
     * @param model - The ModelMap is used to parse objects from controller to HTML using Thymeleaf
     * @return A String which is the HTML checkout form template
     * @throws Exception
     */
    @GetMapping("/checkout")
    public String checkout(ModelMap model) throws Exception {
	    //Get the Account of the currently logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        //Get the list of all carts belonging to the current user
        List<Cart> cartList = cartService.getAllCarts(user);
        //Create an iterator to iterate through the cart list
        Iterator<Cart> cartIterator = cartList.listIterator();

        //Send the cart list to the frontend template for Thymeleaf to process
        model.addAttribute("cartItems", cartList);

        float cartSum = 0;
        int cartQty = 0;
        cartQty = cartList.size();
        if (cartQty==0){
            //If the cart is empty, prevent the user from checking out
            return "redirect:/shopping-cart?empty=true";
        }
        while (cartIterator.hasNext()){
            //Add the subtotal of the cart to the sum amount
            cartSum += cartIterator.next().getSmallSum();
        }

        //Send the cart sum and qty to the frontend template for Thymeleaf to process
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);

        return "checkout";
    }
    
    /**
     * Mapping to add an order to database
     * @return A String which redirects back to the shopping cart
     */
    @PostMapping("/checkout/add")
    public String placeNewOrder() {
	    //Get the currently logged in user Account
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);
        //Create a new order for that user
        orderService.addOrder(user);
        //Get the list of all carts belonging to that user
        List<Cart> cartList = cartService.getAllCarts(user);
        //Create an iterator to iterate through the cart list
        Iterator<Cart> cartIterator = cartList.listIterator();
        while (cartIterator.hasNext()){
            //Empty the cart list after the user checked out by deleting the carts
            cartService.deleteCart(cartIterator.next().getId());
        }
        return "redirect:/shopping-cart";
    }
    
    /**
     * Mapping to display full order list of all customers when path is accessed
     * @param model - The ModelMap is used to parse objects from controller to HTML using Thymeleaf
     * @return A String which is the HTML orderlist form template
     */
    @RequestMapping(value="/orderlist", method=RequestMethod.GET)
    public String showOrderList(ModelMap model){
        List<Order> orderList = orderService.getAllOrders();
        orderList.sort(Comparator.comparing(Order::getId)); // sort order list by id number (low to high id number)
        model.addAttribute("orderList",orderList);
        unifiedService.getCartInfo(model);
        return "orderlist";
    }
    
    /**
     * Mapping to display order details when path is accessed
     * @param model - The ModelMap is used to parse objects from controller to HTML using Thymeleaf
     * @param id - the ID of the order to be displayed
     * @return - A string which is the HTML of the processed order-details template
     */
    @RequestMapping(value="orderlist/order-details/{id}", method=RequestMethod.GET)
    public String showOrderDetail(ModelMap model, @PathVariable Long id){
        Order order = orderService.getOrderById(id).get();
        String itemString = order.getItems();
        itemString = itemString.substring(1, itemString.length() - 1);
        String[] items = itemString.split("\\Q},{\\E");
        ArrayList<String[]> itemInfo = new ArrayList<>();
        Account accountOfOrder = order.getAccount();
        int totalQuantity = 0;
        for(String item: items) {
        	String[] iteminfo = item.split(",");
        	iteminfo[0] = itemService.getItem(Long.valueOf(iteminfo[0])).get().getItemName();
        	itemInfo.add(iteminfo);
            totalQuantity += Integer.parseInt(iteminfo[2]);
        }
        model.addAttribute("itemInfo",itemInfo);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("order", order);
        model.addAttribute("account", accountOfOrder);
        unifiedService.getCartInfo(model);
        return "order-details";
    }
    	
    /**
     * Mapping to change an order status to "Confirmed"
     * @param id - ID of the order to be processed
     * @return a String which redirects back to the orderlist
     */
    @RequestMapping(value="orderlist/confirm-order/{id}", method= RequestMethod.GET)
    public String confirmorder(@PathVariable Long id){
        orderService.confirmOrder(id);
        return "redirect:/orderlist";
    }
    
    /**
     * Mapping to change an order status to "Cancelled"
     * @param id - ID of the order to be processed
     * @return a String which redirects back to the orderlist
     */
    @RequestMapping(value="orderlist/cancel-order/{id}", method= RequestMethod.GET)
    public String unconfirmOrder(@PathVariable Long id){
        orderService.cancelOrder(id);
        return "redirect:/orderlist";
    }
    
    /**
     * Mapping to change an order status to "Processed"
     * @param id - ID of the order to be processed
     * @return a String which redirects back to the orderlist
     */
    @RequestMapping(value="orderlist/process-order/{id}", method= RequestMethod.GET)
    public String processedOrder(@PathVariable Long id){
        orderService.processedOrder(id);
        return "redirect:/orderlist";
    }
    
    /**
     * Mapping to change an order status to "Being Delivered"
     * @param id - ID of the order to be processed
     * @return a String which redirects back to the orderlist
     */
    @RequestMapping(value="orderlist/beingDelivered-order/{id}", method= RequestMethod.GET)
    public String beingDeliveredOrder(@PathVariable Long id){
        orderService.beingDeliveredOrder(id);
        return "redirect:/orderlist";
    }
    
    /**
     * Mapping to change an order status to "Delivered"
     * @param id - ID of the order to be processed
     * @return a String which redirects back to the orderlist
     */
    @RequestMapping(value="orderlist/deliver-order/{id}", method= RequestMethod.GET)
    public String deliveredOrder(@PathVariable Long id){
        orderService.deliveredOrder(id);
        return "redirect:/orderlist";
    }
}
