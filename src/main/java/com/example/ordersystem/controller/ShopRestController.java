package com.example.ordersystem.controller;

import com.example.ordersystem.model.Shop;
import com.example.ordersystem.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopRestController {
    @Autowired
    private ShopService shopService;

    @PostMapping("/shopitem")
    public void saveShopItem(@RequestBody Shop shop){
        shopService.saveShop(shop);
    }
}
