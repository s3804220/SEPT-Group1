package com.example.ordersystem.service;

import com.example.ordersystem.model.*;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class CartServiceTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    private ItemService itemService;

    @Autowired
    public AccountService accountService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        cartRepository.deleteAll();
    }

    @Test
    public void addShop() {

        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        int addedAmount = cartService.addItem(testItem1.getId(), amount, testUser1);

        assertTrue(addedAmount >= 5);
    }

    @Test
    public void getCart() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        int addedAmount = cartService.addItem(testItem1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        Cart testCart2 = cartService.getCart(testCart1.getId());

        assertEquals(testCart1, testCart2);
    }

    @Test
    public void getAllCarts() {

        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        Item testItem2 = new Item("testCake2", "Second test", "product-2.jpg", new BigDecimal("22.00"));
        itemService.saveItem(testItem2);

        cartService.addItem(testItem2.getId(), amount, testUser1);
        Cart testCart2 = cartRepository.findByAccountAndItem(testUser1, testItem2);

        List<Cart> cartlist = cartService.getAllCarts(testUser1);

        assertTrue(cartlist.contains(testCart1));
        assertTrue(cartlist.contains(testCart2));
    }

    @Test
    public void deleteCart() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        int before = cartService.getAllCarts(testUser1).size();

        cartService.deleteCart(testCart1.getId());

        int after = cartService.getAllCarts(testUser1).size();

        assertTrue(before != after);
    }

    @Test
    public void findCartById() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        Cart testCart2 = cartService.findCartById(testCart1.getId());

        assertEquals(testCart1, testCart2);
    }

    @Test
    public void updateAmount() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        cartService.addItem(testItem1.getId(), amount, testUser1);

        int addedAmount = cartService.updateAmount(2, testItem1.getId(), testUser1);

        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        assertEquals(amount, testCart1.getAmount());
    }
}