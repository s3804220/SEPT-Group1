package com.example.ordersystem;

import com.example.ordersystem.controller.*;
import com.example.ordersystem.model.*;
import com.example.ordersystem.repository.*;
import com.example.ordersystem.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.init-db", havingValue="true")
@AllArgsConstructor
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private ShopService shopService;
    @Autowired
    private CartService cartService;

    private ShopRepository shopRepository;
    private CartRepository cartRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Shop> shopList = shopService.getAllShops();


        Shop shop1 = new Shop("DBcake1", new BigDecimal("32.00"), "Good day", "product-1.jpg");
        Shop shop2 = new Shop("DBcake2", new BigDecimal("31.00"),"Good night", "product-2.jpg");
    }

}