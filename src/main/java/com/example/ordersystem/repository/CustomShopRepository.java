package com.example.ordersystem.repository;

import com.example.ordersystem.model.Shop;

import java.util.Optional;

public interface CustomShopRepository {
    Optional<Shop> findById(Long id);
    Shop findShopById(Long id);
}
