package com.example.ordersystem.repository;

import com.example.ordersystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class is the repository of Account which uses the JPARepository interface
 * It provides basic CRUD methods and queries for the Account entity
 */

@Repository
@Transactional(readOnly=true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAccount(String email);
}
