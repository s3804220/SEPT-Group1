package com.example.ordersystem.service;

import com.example.ordersystem.model.*;
import com.example.ordersystem.repository.AccountRepository;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.ItemRepository;
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
import org.springframework.util.FileSystemUtils;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
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
    private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private AccountRepository accountRepository;
    
    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() throws Exception {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        itemRepository.deleteAll();
        accountRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Paths.get("target\\classes\\static\\img\\upload".replace("\\", File.separator)).toFile());
    }
    
	@Test
	public void testAddOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
		int price = orderService.addOrder(testUser1);
		
		assertTrue(price == 55);
	}

	@Test
	public void testConfirmOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
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
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
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
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		itemService.saveItem(testItem1);
		Item testItem2 = new Item("testCake2", "Second test", "product-2.jpg", new BigDecimal("12.00"),"Cake",true);
        itemService.saveItem(testItem2);
        cartService.addItem(testItem1.getId(), 5, testUser1);
		orderService.addOrder(testUser1);
		cartService.addItem(testItem2.getId(), 4, testUser2);
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
