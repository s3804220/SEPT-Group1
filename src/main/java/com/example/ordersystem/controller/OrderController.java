package com.example.ordersystem.controller;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

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
import com.example.ordersystem.service.ItemService;

@Controller
public class OrderController {
	
	private OrderService orderService;
	private AccountService accountService;
	private CartService cartService;
	private ItemService itemService;
	
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
    
    @GetMapping("/checkout")
    public String checkout(ModelMap model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();

        Account user = accountService.getAccountById(userId);
        List<Cart> cartList = cartService.getAllCarts(user);
        model.addAttribute("cartItems", cartList);

        float cartSum = 0;
        int cartQty = 0;
        cartQty = cartList.size();
        for (Cart cart : cartList) {
            cartSum += cart.getSmallSum();
        }
        model.addAttribute("cartSum",cartSum);
        model.addAttribute("cartQty",cartQty);

        return "checkout";
    }
    
    @PostMapping("/checkout/add")
    public String addItemToCart() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInAcc = (Account)auth.getPrincipal();
        Long userId = loggedInAcc.getId();
        Account user = accountService.getAccountById(userId);
        orderService.addOrder(user);
        List<Cart> cartList = cartService.getAllCarts(user);
        for (Cart cart : cartList){
            cartService.deleteCart(cart.getId());
        }
        return "redirect:/shopping-cart";
    }
    
    @RequestMapping(value="/orderlist", method=RequestMethod.GET)
    public String showOrderList(ModelMap model){
        List<Order> orderList = orderService.getAllOrders();
        orderList.sort(Comparator.comparing(Order::getId)); // sort order list by id number (low to high id number)
        model.addAttribute("orderList",orderList);
        return "orderlist";
    }
    
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
        return "order-details";
    }
    	
    @RequestMapping(value="orderlist/confirm-order/{id}", method= RequestMethod.GET)
    public String confirmorder(@PathVariable Long id){
        orderService.confirmOrder(id);
        return "redirect:/orderlist";
    }

    @RequestMapping(value="orderlist/cancel-order/{id}", method= RequestMethod.GET)
    public String unconfirmOrder(@PathVariable Long id){
        orderService.cancelOrder(id);
        return "redirect:/orderlist";
    }
    
    @RequestMapping(value="orderlist/process-order/{id}", method= RequestMethod.GET)
    public String processedOrder(@PathVariable Long id){
        orderService.processedOrder(id);
        return "redirect:/orderlist";
    }
    
    @RequestMapping(value="orderlist/beingDelivered-order/{id}", method= RequestMethod.GET)
    public String beingDeliveredOrder(@PathVariable Long id){
        orderService.beingDeliveredOrder(id);
        return "redirect:/orderlist";
    }
    
    @RequestMapping(value="orderlist/deliver-order/{id}", method= RequestMethod.GET)
    public String deliveredOrder(@PathVariable Long id){
        orderService.deliveredOrder(id);
        return "redirect:/orderlist";
    }
}
