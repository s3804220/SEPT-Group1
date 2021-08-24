package com.example.ordersystem.service;

import java.util.List;
import java.util.ArrayList;

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
    
    @Autowired
    public OrderService(CartRepository cartRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }
    
    public int addOrder(Account user){
      List<Cart> carts = cartRepository.findByAccount(user);
      ArrayList<String> items = new ArrayList<String>();
      int price = 0;
      for(Cart cart: carts) {
    	  price += cart.getSmallSum();
    	  Item item = cart.getItem();
    	  String iteminfo = item.getId().toString();
    	  iteminfo += ','+item.getItemPrice().toString();
    	  iteminfo += ','+cart.getAmount();
    	  items.add(iteminfo);
    	  
      }
      Order order = new Order();
      order.setPrice(price);
      order.setAccount(user);
      order.setConfirm(false);
      order.setItems(items);
      orderRepository.save(order);
      return price;
  }
    
    public void confirmOrder(Account user) {
    	Order order = orderRepository.findByAccount(user);
    	order.setConfirm(true);
    }
    
    public void unconfirmOrder(Account user) {
    	Order order = orderRepository.findByAccount(user);
    	order.setConfirm(false);
    }
    
    public List<Order> getAllOrders() {
    	return orderRepository.findAll();
    }
}
