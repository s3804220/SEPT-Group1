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
    public void setUp() {

    }
    
    @After
    public void tearDown() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        itemRepository.deleteAll();
        accountRepository.deleteAll();
    }
    
	@Test
	public void testAddOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
		Order order = orderService.addOrder(testUser1);
		
		assertTrue(order.getPrice().floatValue() == 55.00f);
	}

	@Test
	public void testConfirmOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
		Order order1 = orderService.addOrder(testUser1);
		orderService.confirmOrder(order1.getId());
		List<Order> orders = orderService.getAllOrders();
		Order target = null;
		for(Order order: orders) {
			if (order.getAccount().getEmail() == "test@gmail.com") {
				target = order;
			}
		}
		assertTrue(target.getStatus() == "Confirmed");
	}

	@Test
	public void testUnconfirmOrder() {
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		accountService.signUpAccount(testUser1);
		itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), 5, testUser1);
		Order order1 = orderService.addOrder(testUser1);
		orderService.confirmOrder(order1.getId());
		orderService.cancelOrder(order1.getId());
		List<Order> orders = orderService.getAllOrders();
		Order target = null;
		for(Order order: orders) {
			if (order.getAccount().getEmail() == "test@gmail.com") {
				target = order;
			}
		}
		assertTrue(target.getStatus() == "Cancelled");
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

	@Test
	public void testGetOrderByAccountId(){
		// create mock user
		Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
		accountService.signUpAccount(testUser1);

		// create mock items
		Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
		itemService.saveItem(testItem1);
		Item testItem2 = new Item("testCake2", "Second test", "product-2.jpg", new BigDecimal("12.00"),"Cake",true);
		itemService.saveItem(testItem2);

		// add item1 with quantity of 2 (total price = 11*2 = 22) to cart and check out first order
		cartService.addItem(testItem1.getId(), 2, testUser1);
		orderService.addOrder(testUser1);

		// add item2 with quantity of 3 (total price = 12*3 = 36) to cart and check out second order
		cartService.addItem(testItem2.getId(), 3, testUser1);
		orderService.addOrder(testUser1);

		List<Order> testUser1OrderList = orderService.getOrdersByAccountId(testUser1.getId());
		for (Order order : testUser1OrderList){
			assertEquals(order.getAccount(), testUser1); // check if each order in testUser1OrderList is made by testUser1
		}
	}
}
