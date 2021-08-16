package com.example.ordersystem.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.OrderRepository;

@Transactional
@Service
public class OrderService {
	
	@PersistenceContext
    private EntityManager em;

    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private AccountService accountService;

    @Autowired
    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, AccountService accountService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.accountService = accountService;
    }
    
    public int addOrder(Account user){
      List<Cart> carts = cartRepository.findByAccount(user);
      Order order = orderRepository.findByAccount(user);
      int price = 0;
      for(Cart cart: carts) {
    	  price += cart.getSmallSum();
      }
      if(order != null) {
          order.setPrice(price);
      } else {
          order = new Order();
          order.setPrice(price);
          order.setAccount(user);
          order.setConfirm(false);
      }
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
