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

    public Order addOrder(Account user) {
        List<Cart> carts = cartRepository.findByAccount(user);
        String items = "";
        float tmp = 0;
        for (Cart cart : carts) {
            tmp += cart.getSmallSum();
            Item item = cart.getItem();
            items += "{" + item.getId().toString();
            items += ',' + item.getItemPrice().toString();
            String amount = "" + cart.getAmount();
            items += ',' + amount + "},";
        }
        BigDecimal price = new BigDecimal(Float.toString(tmp));
        items = items.substring(0, items.length() - 1);
        Order order = new Order();
        order.setPrice(price);
        order.setAccount(user);
        order.setStatus("Unconfirmed");
        order.setItems(items);
        orderRepository.save(order);
        emailService.sendEmail("created",order);
        return order;
    }

    public void confirmOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Confirmed");
        emailService.sendEmail("confirmed",order);
    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Cancelled");
        emailService.sendEmail("cancelled",order);
    }

    public void processedOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Processed");
        emailService.sendEmail("processed",order);
    }

    public void beingDeliveredOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("In Delivery");
        emailService.sendEmail("delivering",order);
    }

    public void deliveredOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Delivered");
        emailService.sendEmail("delivered",order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByAccountId(Long id) {
        List<Order> accountOrders = new ArrayList<>();
        Iterator<Order> orderIterator = getAllOrders().listIterator();
        while (orderIterator.hasNext()){
            Order nextOrder = orderIterator.next();
            if(nextOrder.getAccount().getId().equals(id)){
                accountOrders.add(nextOrder);
            }
        }

        return accountOrders;
    }
}
