package com.example.ordersystem.service;

import com.example.ordersystem.model.Shop;
import com.example.ordersystem.repository.ShopRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopRepository shopRepository;
    private ShopService shopService;


//        testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "product-1.jpg");
//        testShop2 = new Shop("testCake2", new BigDecimal("8.00"),"Last test", "product-2.jpg");


    @Test
    public void saveShop() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "product-1.jpg");

        Shop saved = shopRepository.save(testShop1);

        Shop found = shopRepository.findShopById(testShop1.getId());

        assertEquals(saved, found);
    }


    @Test
    public void findShopById() {
    }

    @Test
    public void getAllShops() {
    }

    @Test
    public void deleteShop() {
    }

    @Test
    public void findTotal() {
    }

    @Test
    public void findListPaging() {
    }
}