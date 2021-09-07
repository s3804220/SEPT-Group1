package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * This class is the service layer for performing CRUD operations on Orders
 */
@Transactional
@Service
public class OrderService {

    @NonNull
    @Lazy
    private CartRepository cartRepository;
    @NonNull
    @Lazy
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    public OrderService(@NonNull @Lazy CartRepository cartRepository, @NonNull @Lazy OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Method to create a new order for a specific user and save it into database
     * @param user - The user Account to whom the order belongs
     * @return The newly created Order object
     */
    public Order addOrder(Account user) {
        //Get all current carts of the specified user account
        List<Cart> carts = cartRepository.findByAccount(user);
        String items = "";
        float tmp = 0;
        //Create an iterator to iterate through the cart list
        Iterator<Cart> cartIterator = carts.listIterator();
        while(cartIterator.hasNext()){
            //Get the next cart object in the list
            Cart nextCart = cartIterator.next();
            //Add the subtotal of the cart
            tmp += nextCart.getSmallSum();
            //Get the item of that cart and its details (ID, price, amount)
            Item item = nextCart.getItem();
            items += "{" + item.getId().toString();
            items += ',' + item.getItemPrice().toString();
            String amount = "" + nextCart.getAmount();
            items += ',' + amount + "},";
        }
        BigDecimal price = new BigDecimal(Float.toString(tmp));
        items = items.substring(0, items.length() - 1);
        //Create a new order from all the cart information above
        Order order = new Order();
        order.setPrice(price);
        order.setAccount(user);
        order.setStatus("Unconfirmed");
        order.setItems(items);
        //Save the new order into database
        orderRepository.save(order);
        //Send an email to the user's email address to confirm the creation of the order
        emailService.sendEmail("created",order);
        return order;
    }

    /**
     * Method to change the status of a specific order to "Confirmed",
     * and send an email to the user's email address to notify them of the status
     * @param id - The ID of the order to change
     */
    public void confirmOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Confirmed");
        emailService.sendEmail("confirmed",order);
    }

    /**
     * Method to change the status of a specific order to "Cancelled",
     * and send an email to the user's email address to notify them of the status
     * @param id - The ID of the order to change
     */
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Cancelled");
        emailService.sendEmail("cancelled",order);
    }

    /**
     * Method to change the status of a specific order to "Processed",
     * and send an email to the user's email address to notify them of the status
     * @param id - The ID of the order to change
     */
    public void processedOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Processed");
        emailService.sendEmail("processed",order);
    }

    /**
     * Method to change the status of a specific order to "In delivery",
     * and send an email to the user's email address to notify them of the status
     * @param id - The ID of the order to change
     */
    public void beingDeliveredOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("In Delivery");
        emailService.sendEmail("delivering",order);
    }

    /**
     * Method to change the status of a specific order to "Delivered",
     * and send an email to the user's email address to notify them of the status
     * @param id - The ID of the order to change
     */
    public void deliveredOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Delivered");
        emailService.sendEmail("delivered",order);
    }

    /**
     * Method to get a list of all orders in the database
     * @return The List of all orders found
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Method to get a specific order by ID
     * @param id - The ID of the order to get
     * @return The Optional object which may contain the order to get
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Method to get all orders of a specified account ID
     * @param id - The ID of the account from which to get all orders
     * @return The List of all orders belonging to the specified account
     */
    public List<Order> getOrdersByAccountId(Long id) {
        List<Order> accountOrders = new ArrayList<>();
        //Create an iterator to iterate through the list of all orders
        Iterator<Order> orderIterator = getAllOrders().listIterator();
        //Get the next Order object in the list, if the account ID matches the specified ID,
        //add it to the final list
        while (orderIterator.hasNext()){
            Order nextOrder = orderIterator.next();
            if(nextOrder.getAccount().getId().equals(id)){
                accountOrders.add(nextOrder);
            }
        }

        return accountOrders;
    }
}
