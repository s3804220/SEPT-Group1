package com.example.ordersystem.repository;

import com.example.ordersystem.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CustomCartRepository {

//    int sumPrice(String userId);
    Cart findByUserIdAndShopId(String userid, Long shopId);
//    int countCart(Long productId, String userId); // Find same items in cart, and count
//    void updateCart(Long id, int amount); // Change the number of items
}

