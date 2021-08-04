package com.example.ordersystem;

import com.example.ordersystem.exception.account.AccountNotFoundException;
import com.example.ordersystem.exception.account.EmailAlreadyTakenException;
import com.example.ordersystem.exception.account.InvalidEmailFormatException;
import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.service.AccountService;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceTest {
    @Autowired
     public AccountService accountService;

    @Test
    public void loadUserByUsername() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Account testUser3 = new Account("Bernie", "Sanders", "234 Testing Lane", "0903682439", "not@signup.vn", "password", AccountRole.USER);

        accountService.signUpAccount(testUser1);

        assertEquals(testUser1, accountService.loadUserByUsername(testUser1.getEmail()));
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(testUser3.getEmail())); //User not signed up
    }

    @Test
    public void signUpAccount() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Account testUser2 = new Account("Grace", "Heather", "321 Testing Lane", "0903682439", "invalid#email.com", "password", AccountRole.USER);

        assertEquals(testUser1, accountService.signUpAccount(testUser1));
        assertThrows(EmailAlreadyTakenException.class, () -> accountService.signUpAccount(testUser1)); // Email already taken
        assertThrows(InvalidEmailFormatException.class, () -> accountService.signUpAccount(testUser2)); // Invalid email format
    }

    @Test
    public void getAllAccounts() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Account testUser2 = new Account("Grace", "Heather", "321 Testing Lane", "0903682439", "test2@email.com", "password", AccountRole.USER);
        Account testUser3 = new Account("Bernie", "Sanders", "234 Testing Lane", "0903682439", "test3@yahoo.com", "password", AccountRole.USER);
        List<Account> allAccounts = new ArrayList<>();

        accountService.signUpAccount(testUser1);
        accountService.signUpAccount(testUser2);
        accountService.signUpAccount(testUser3);

        allAccounts.add(testUser1);
        allAccounts.add(testUser2);
        allAccounts.add(testUser3);

        assertTrue(accountService.getAllAccounts().containsAll(allAccounts));
        accountService.deleteAccount(testUser3.getId());
        assertFalse(accountService.getAllAccounts().containsAll(allAccounts));
    }

    @Test
    public void getAccountById() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);

        accountService.signUpAccount(testUser1);

        assertEquals(testUser1, accountService.getAccountById(testUser1.getId()));
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(999L)); //Invalid ID
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(-999L)); //Invalid ID
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(0L)); //Invalid ID
    }

    @Test
    public void deleteAccount() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);

        accountService.signUpAccount(testUser1);

        assertEquals(testUser1, accountService.deleteAccount(testUser1.getId()));
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(testUser1.getId())); // User is deleted
    }

    @Test
    public void updateAccount() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);
        Account updatedTestUser1 = new Account("Updated First Name", "Updated Last Name", "999 Updated Street", "0506285361", "updated@gmail.com", "updated", AccountRole.USER);

        accountService.signUpAccount(testUser1);
        updatedTestUser1.setId(testUser1.getId());

        assertEquals(updatedTestUser1, accountService.updateAccount(testUser1.getId(), updatedTestUser1));
    }

    @Test
    public void setAccountRole() {
        Account testUser1 = new Account("Mike", "Dean", "123 Testing Lane", "0903682439", "test@gmail.com", "password", AccountRole.USER);

        accountService.signUpAccount(testUser1);

        assertEquals(accountService.setAccountRole(testUser1.getId(), AccountRole.ADMIN).getAccountRole(), testUser1.getAccountRole());
        assertEquals(accountService.setAccountRole(testUser1.getId(), AccountRole.ADMIN).getAccountRole(), AccountRole.ADMIN);
    }
}