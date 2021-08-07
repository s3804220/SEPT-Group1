package com.example.ordersystem;

import com.example.ordersystem.controller.*;
import com.example.ordersystem.model.*;
import com.example.ordersystem.repository.*;
import com.example.ordersystem.security.*;
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
    private AccountService accountService;

    private AccountRepository accountRepository;

    @Autowired
    private ShopService shopService;


    private ShopRepository shopRepository;
    private CartRepository cartRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Account> accountList = accountService.getAllAccounts();

//        for (Account temp : accountList){
//            accountService.deleteAccount(temp.getId());
//        }
//        this.accountRepository.deleteAll();

        //Initialize admin account
        Account admin = new Account("John", "Doe", "123 Tech Street", "0204648395", "admin@gmail.com", "admin", AccountRole.ADMIN);
        accountService.signUpAccount(admin);
        accountService.setAccountRole(admin.getId(), AccountRole.ADMIN);

        //Initialize user account
        Account user1 = new Account("Jeffrey", "Babble", "456 Flower Lane", "0903682439", "user@gmail.com", "password", AccountRole.USER);
        accountService.signUpAccount(user1);




        Shop shop1 = new Shop("DBcake1", new BigDecimal("32.00"), "Good day", "product-1.jpg");
        Shop shop2 = new Shop("DBcake2", new BigDecimal("31.00"),"Good night", "product-2.jpg");
        shopService.saveShop(shop1);
        shopService.saveShop(shop2);


        Cart cart = new Cart();
        cart.setAccount(user1);
        cart.setShop(shop1);
        cart.setAmount(100);

        cartRepository.save(cart);
    }

}
