package com.example.ordersystem.repository;

import com.example.ordersystem.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, CustomShopRepository {

    Optional<Shop> findById(Long id);
    Shop findShopById(Long id);
}
