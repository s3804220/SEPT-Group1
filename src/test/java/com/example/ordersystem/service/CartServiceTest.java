package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.model.Cart;
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
    private ShopService shopService;

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
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        int addedAmount = cartService.addShop(testShop1.getId(), amount, testUser1);

        assertTrue(addedAmount >= 5);
    }

    @Test
    public void getCart() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        int addedAmount = cartService.addShop(testShop1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndShop(testUser1, testShop1);

        Cart testCart2 = cartService.getCart(testCart1.getId());

        assertEquals(testCart1, testCart2);
    }

    @Test
    public void getAllCarts() {

        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndShop(testUser1, testShop1);


        Shop testShop2 = new Shop("testCake2", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        shopService.saveShop(testShop2);

        cartService.addShop(testShop2.getId(), amount, testUser1);
        Cart testCart2 = cartRepository.findByAccountAndShop(testUser1, testShop2);

        List<Cart> cartlist = cartService.getAllCarts(testUser1);

        assertTrue(cartlist.contains(testCart1));
        assertTrue(cartlist.contains(testCart2));
    }

    @Test
    public void deleteCart() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndShop(testUser1, testShop1);

        int before = cartService.getAllCarts(testUser1).size();

        cartService.deleteCart(testCart1.getId());

        int after = cartService.getAllCarts(testUser1).size();

        assertTrue(before != after);
    }

    @Test
    public void findCartById() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), amount, testUser1);
        Cart testCart1 = cartRepository.findByAccountAndShop(testUser1, testShop1);

        Cart testCart2 = cartService.findCartById(testCart1.getId());

        assertEquals(testCart1, testCart2);
    }

    @Test
    public void updateAmount() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        int amount = 5;
        accountService.signUpAccount(testUser1);
        shopService.saveShop(testShop1);
        cartService.addShop(testShop1.getId(), amount, testUser1);

        int AddedAmount = cartService.updateAmount(2, testShop1.getId(), testUser1);

        Cart testCart1 = cartRepository.findByAccountAndShop(testUser1, testShop1);

        assertEquals(amount, testCart1.getAmount());
    }
}