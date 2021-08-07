package com.example.ordersystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	Order findByAccount(Account account);
}
