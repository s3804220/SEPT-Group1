package com.example.ordersystem.repository;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CustomCartRepository {

//    int sumPrice(String userId);
    List<Cart> findByAccount(Account account);
    Cart findByAccountAndItem(Account account, Item item);

    @Query("UPDATE Cart c SET c.amount = ?1 WHERE c.item.id = ?2 AND c.account.id = ?3")
    @Modifying
    int updateAmount(int amount, Long itemId, Long accountId);
}

