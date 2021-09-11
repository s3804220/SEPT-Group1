package com.example.ordersystem.repository;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class is the repository of ItemImage which uses the JPARepository interface
 * It provides basic CRUD methods and queries for the ItemImage entity
 */
@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItem(Item item);
}
