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

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.init-db", havingValue="true")
@AllArgsConstructor
public class DbInitializer implements CommandLineRunner {
    @Autowired
    private AccountService accountService;

    private AccountRepository accountRepository;

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
    }

}
