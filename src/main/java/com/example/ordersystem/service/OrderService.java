package com.example.ordersystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.OrderRepository;

@Transactional
@Service
public class OrderService {
	
	@PersistenceContext
    private EntityManager em;
	
	@NonNull
	@Lazy
    private CartRepository cartRepository;
	@NonNull
	@Lazy
    private OrderRepository orderRepository;
    private AccountService accountService;

    @Autowired
    public OrderService(@NonNull @Lazy CartRepository cartRepository, @NonNull @Lazy OrderRepository orderRepository, @NonNull @Lazy AccountService accountService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.accountService = accountService;
    }
    
    public Order addOrder(Account user){
      List<Cart> carts = cartRepository.findByAccount(user);
      String items = "";
      float tmp = 0;
      for(Cart cart: carts) {
    	  tmp += cart.getSmallSum();
    	  Item item = cart.getItem();
    	  items +=  "{"+item.getId().toString();
    	  items += ','+item.getItemPrice().toString();
    	  String amount = ""+cart.getAmount();
    	  items += ','+amount+"},";    	  
      }      
      BigDecimal price = new BigDecimal(Float.toString(tmp));
      items = items.substring(0, items.length()-1);
      Order order = new Order();
      order.setPrice(price);
      order.setAccount(user);
      order.setStatus("Unconfirmed");
      order.setItems(items);
      orderRepository.save(order);
      return order;
  }
    
    public void confirmOrder(Long id) {
    	Order order = orderRepository.findById(id).get();
    	order.setStatus("Confirmed");
    }
    
    public void cancelOrder(Long id) {
    	Order order = orderRepository.findById(id).get();
    	order.setStatus("Cancelled");
    }
    
    public void processedOrder(Long id) {
    	Order order = orderRepository.findById(id).get();
    	order.setStatus("Processed");
    }
    
    public void beingDeliveredOrder(Long id) {
    	Order order = orderRepository.findById(id).get();
    	order.setStatus("In Delivery");
    }
    
    public void deliveredOrder(Long id) {
    	Order order = orderRepository.findById(id).get();
    	order.setStatus("Delivered");
    }
    
    public List<Order> getAllOrders() {
    	return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(Long id) {
    	return orderRepository.findById(id);
    }
    
    public List<Order> getOrdersByAccountId(Long id){
        List<Order> accountOrders = new ArrayList<>();
        for(Order order : getAllOrders()){
            if (order.getAccount().getId().equals(id)){
                accountOrders.add(order);
            }
        }
        return accountOrders;
    }
}
