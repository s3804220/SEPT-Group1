package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final String INVALID_EMAIL_ADDRESS = "This email format is invalid";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpAccount(Account account){
        boolean accountExists = accountRepository.findByEmail(account.getEmail()).isPresent();
        if (accountExists){
            throw new IllegalStateException("email already taken");
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(account.getEmail());
        if (!matcher.find()){
            throw new IllegalStateException(INVALID_EMAIL_ADDRESS);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        accountRepository.save(account);
        accountRepository.enableAccount(account.getEmail());
        return account.toString();
    }
    public int enableAccount(String email){
        return accountRepository.enableAccount(email);
    }
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Can find account with this id"));
    }
    public Account deleteAccount(Long id){
        Account accountToDelete = getAccountById(id);
        accountRepository.delete(accountToDelete);
        return accountToDelete;
    }
    public Account updateAccount(Long id, Account account){
        Account accountToUpdate = getAccountById(id);

        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setAccAddress(account.getAccAddress());
        accountToUpdate.setPhone(account.getPhone());
        accountToUpdate.setPassword(account.getPassword());
        accountToUpdate.setEmail(account.getEmail());

        accountRepository.save(accountToUpdate);
        
        return accountToUpdate;
    }
    public Account setAccountRole(Long id, AccountRole accountRole){
        Account accountToUpdate = getAccountById(id);
        accountToUpdate.setAccountRole(accountRole);
        return accountToUpdate;
    }

}
