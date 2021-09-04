package com.example.ordersystem.repository;

import com.example.ordersystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This class is the repository of Item which uses the JPARepository interface
 * It provides basic CRUD methods and queries for the Item entity
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
