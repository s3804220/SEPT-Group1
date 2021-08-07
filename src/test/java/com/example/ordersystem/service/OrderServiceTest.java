package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Order;
import com.example.ordersystem.model.Shop;
import com.example.ordersystem.repository.CartRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.ordersystem.repository.OrderRepository;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class OrderServiceTest {
	
	@Autowired
    OrderRepository orderRepository;
	
    @Autowired
    private OrderService orderService;
    
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;
    
    @Autowired
    public AccountService accountService;
    
    @Autowired
    private ShopService shopService;
    
    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() throws Exception {
        orderRepository.deleteAll();
    }
    
	@Test
	public void testAddOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
		accountService.signUpAccount(testUser1);
		shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), 5, testUser1);
		int price = orderService.addOrder(testUser1);
		
		assertTrue(price == 55);
	}

	@Test
	public void testConfirmOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
		accountService.signUpAccount(testUser1);
		shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), 5, testUser1);
		orderService.addOrder(testUser1);
		orderService.confirmOrder(testUser1);
		List<Order> orders = orderService.getAllOrders();
		Order target = null;
		for(Order order: orders) {
			if (order.getAccount().getEmail() == "test@gmail.com") {
				target = order;
			}
		}
		assertTrue(target.isConfirm());
	}

	@Test
	public void testUnconfirmOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
		accountService.signUpAccount(testUser1);
		shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), 5, testUser1);
		orderService.addOrder(testUser1);
		orderService.confirmOrder(testUser1);
		orderService.unconfirmOrder(testUser1);
		List<Order> orders = orderService.getAllOrders();
		Order target = null;
		for(Order order: orders) {
			if (order.getAccount().getEmail() == "test@gmail.com") {
				target = order;
			}
		}
		assertFalse(target.isConfirm());
	}

	@Test
	public void testGetAllOrders() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		accountService.signUpAccount(testUser1);
		Account testUser2 = new Account("Alley", "Cat", "112 Testing Lane", "0933684439", "test2@gmail.com", "password2", AccountRole.USER);
		accountService.signUpAccount(testUser2);
		Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
		shopService.saveShop(testShop1);
		Shop testShop2 = new Shop("testCake2", new BigDecimal("12.00"), "Frist test", "img/shop/product-2.jpg");
        shopService.saveShop(testShop2);
        cartService.addShop(testShop1.getId(), 5, testUser1);
		orderService.addOrder(testUser1);
		cartService.addShop(testShop2.getId(), 4, testUser2);
		orderService.addOrder(testUser2);
		List<Order> orders = orderService.getAllOrders();
		Order target1 = null;
		Order target2 = null;
		for(Order order: orders) {
			if (order.getAccount().getEmail() == "test@gmail.com") {
				target1 = order;
			}
			if (order.getAccount().getEmail() == "test2@gmail.com") {
				target2 = order;
			}
		}
		
		assertTrue(orders.contains(target1));
        assertTrue(orders.contains(target2));
	}

}
