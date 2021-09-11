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
import org.springframework.util.FileSystemUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        //Clear all database after testing
        cartRepository.deleteAll();
        itemRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void addShop() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        int addedAmount = cartService.addItem(testItem1.getId(), amount, testUser1);

        //Assert the amount added was the same as expected
        assertTrue(addedAmount >= 5);
    }

    @Test
    public void getCart() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        int addedAmount = cartService.addItem(testItem1.getId(), amount, testUser1);
        //Get the Cart object that was just created
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        Cart testCart2 = cartService.getCart(testCart1.getId());
        //Assert that the Cart object matches expected value
        assertEquals(testCart1, testCart2);
    }

    @Test
    public void getAllCarts() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        cartService.addItem(testItem1.getId(), amount, testUser1);
        //Get the Cart that was just created
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        //Create another new Item and save it into database
        Item testItem2 = new Item("testCake2", "Second test", "product-2.jpg", new BigDecimal("22.00"),"Cake",true);
        itemService.saveItem(testItem2);

        //Add that new Item into another different cart
        cartService.addItem(testItem2.getId(), amount, testUser1);
        //Get the second newly created cart
        Cart testCart2 = cartRepository.findByAccountAndItem(testUser1, testItem2);

        //Get the list of all carts in the database
        List<Cart> cartlist = cartService.getAllCarts(testUser1);

        //Check that the list returned contains the expected carts
        assertTrue(cartlist.contains(testCart1));
        assertTrue(cartlist.contains(testCart2));
    }

    @Test
    public void deleteCart() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        cartService.addItem(testItem1.getId(), amount, testUser1);
        //Get the Cart that was just created
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        //Get the size of the list of all carts before deleting
        int before = cartService.getAllCarts(testUser1).size();

        //Delete one cart from the database
        cartService.deleteCart(testCart1.getId());

        //Get the size of the list of all carts after deleting
        int after = cartService.getAllCarts(testUser1).size();

        //Assert that the list size has changed after deleting
        assertTrue(before != after);
    }

    @Test
    public void findCartById() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        cartService.addItem(testItem1.getId(), amount, testUser1);
        //Get the Cart that was just created
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        //Use findByID method to get the Cart object
        Cart testCart2 = cartService.findCartById(testCart1.getId());

        //Assert that the findByID method returns the correct Cart
        assertEquals(testCart1, testCart2);
    }

    @Test
    public void updateAmount() {
        //Create a new test Account and a new test Item
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"),"Cake",true);
        int amount = 5;
        accountService.signUpAccount(testUser1);
        itemService.saveItem(testItem1);
        //Use the cartService to add that Item with the specified amount into the Account's cart
        cartService.addItem(testItem1.getId(), amount, testUser1);

        //Call updateAmount to add 2 to the item's amount in the cart
        int addedAmount = cartService.updateAmount(2, testItem1.getId(), testUser1);

        //Get the Cart object that was just updated
        Cart testCart1 = cartRepository.findByAccountAndItem(testUser1, testItem1);

        //Assert that the amount has been correctly updated
        assertEquals(amount, testCart1.getAmount());
    }
}