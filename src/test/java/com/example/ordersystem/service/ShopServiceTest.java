package com.example.ordersystem.service;

import com.example.ordersystem.model.Shop;
import com.example.ordersystem.repository.ShopRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired(required = true)
    private ShopService shopService;

    @BeforeEach
    public void clearStored() {
//        for(int i = 0; i<shopService.getAllShops().size(); i++) {
//            Long id = (long) i;
//            shopService.deleteShop(id);
//        }
        shopRepository.deleteAll();

    }

    @AfterEach
    public void clearData() {
        shopRepository.deleteAll();
    }

    @Test
    public void saveShop() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");

        Shop saved = shopService.saveShop(testShop1);

        Shop found = shopService.findShopById(testShop1.getId());

        assertEquals(saved.getId(), found.getId());
    }


    @Test
    public void findShopById() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        shopService.saveShop(testShop1);
        Shop saved = shopService.findShopById(testShop1.getId());
        assertEquals(testShop1.getId(), saved.getId());
    }

    @Test
    public void getAllShops() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        Shop testShop2 = new Shop("testCake2", new BigDecimal("8.00"),"Last test", "img/shop/product-2.jpg");

        Shop testShop11 = shopService.saveShop(testShop1);
        Shop testShop22 = shopService.saveShop(testShop2);

        Shop shop1 = shopService.findShopById(testShop11.getId());
        Shop shop2 = shopService.findShopById(testShop22.getId());

        List<Shop> shoplist = shopService.getAllShops();
//        int index = shopService.findShopById(testShop1.getId()).get;
        assertTrue(shoplist.contains(shopService.findShopById(shop2.getId())));
//        assertEquals(shoplist.contains(), shop1.getId());
//        assertEquals(shopList.get(0).getPrice(), shopService.getAllShops().get(0).getPrice());
//        assertEquals(shopList.get(0).getDescription(), shopService.getAllShops().get(0).getDescription());
    }

    @Test
    public void deleteShop() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");

        shopService.saveShop(testShop1);

        Shop shop = shopService.deleteShop(testShop1.getId());

        assertEquals(testShop1.getName(), shop.getName());
        assertEquals(testShop1.getPrice(), shop.getPrice());
        assertEquals(testShop1.getDescription(), shop.getDescription());
    }

    @Test
    public void findTotal() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        Shop testShop2 = new Shop("testCake2", new BigDecimal("8.00"),"Last test", "img/shop/product-2.jpg");

        shopService.saveShop(testShop1);
        shopService.saveShop(testShop2);

        int amount = 2;
        int num = shopService.findTotal();

        assertTrue(amount <= shopService.findTotal());
        assertEquals(num, shopService.findTotal());
    }

    @Test
    public void findListPaging() {
        Shop testShop1 = new Shop("testCake1", new BigDecimal("11.00"), "Frist test", "img/shop/product-1.jpg");
        Shop testShop2 = new Shop("testCake2", new BigDecimal("8.00"),"Last test", "img/shop/product-2.jpg");

        shopService.saveShop(testShop1);
        shopService.saveShop(testShop2);

        List<Shop> shopList = shopService.findListPaging(0, 12);

        List<Shop> shopList2 = shopService.getAllShops();

        int amount = shopList2.size()-1;

        assertEquals(shopList2.get(0).getName(), shopList.get(0).getName());
        assertEquals(shopList2.get(0).getPrice(), shopList.get(0).getPrice());
        assertEquals(shopList2.get(0).getDescription(), shopList.get(0).getDescription());

        assertEquals(shopList2.get(amount).getName(), shopList.get(amount).getName());
        assertEquals(shopList2.get(amount).getPrice(), shopList.get(amount).getPrice());
        assertEquals(shopList2.get(amount).getDescription(), shopList.get(amount).getDescription());
    }
}