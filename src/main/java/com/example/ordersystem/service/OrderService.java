package com.example.ordersystem.service;

import java.math.BigDecimal;
import java.util.List;

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
    
    public BigDecimal addOrder(Account user){
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
      order.setStatus("Cancelled");
      order.setItems(items);
      orderRepository.save(order);
      return price;
  }
    
    public void confirmOrder(Account user) {
    	Order order = orderRepository.findByAccount(user);
    	order.setStatus("Confirmed");
    }
    
    public void unconfirmOrder(Account user) {
    	Order order = orderRepository.findByAccount(user);
    	order.setStatus("Cancelled");
    }
    
    public List<Order> getAllOrders() {
    	return orderRepository.findAll();
    }
}
