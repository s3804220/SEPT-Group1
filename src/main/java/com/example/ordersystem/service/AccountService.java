package com.example.ordersystem.service;

import com.example.ordersystem.exception.account.AccountNotFoundException;
import com.example.ordersystem.exception.account.EmailAlreadyTakenException;
import com.example.ordersystem.exception.account.InvalidEmailFormatException;
import com.example.ordersystem.exception.account.InvalidPhoneFormatException;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is the service layer for performing CRUD operations on accounts
 */

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // Password encoder

    // Format for valid email
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", Pattern.CASE_INSENSITIVE);

    // Format for valid phone number
    private static final Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}");

    /**
     * Method to find the details of a user by their email
     * @param email - The email of the user account to find
     * @return The details of the user account
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    /**
     * Method to register an account and encrypt the password when email criteria are met
     * @param account - The new Account object to register
     * @return The new Account object that was just registered
     */
    public Account signUpAccount(Account account){
        boolean accountExists = accountRepository.findByEmail(account.getEmail()).isPresent();
        // if account with provided email already exist then throw exception
        if (accountExists){
            throw new EmailAlreadyTakenException(account.getEmail());
        }
        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(account.getEmail());
        // if provided email has an invalid format then throw exception
        if (!emailMatcher.find()){
            throw new InvalidEmailFormatException(account.getEmail());
        }

        Matcher phoneMatcher = VALID_PHONE_NUMBER_REGEX.matcher(account.getPhone());
        // if provided phone number has an invalid format then throw exception
        if (!phoneMatcher.find()){
            throw new InvalidPhoneFormatException(account.getPhone());
        }
        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword()); // hash user-provided password for security
        account.setPassword(encodedPassword);
        accountRepository.save(account); // system save account
        accountRepository.enableAccount(account.getEmail()); // spring security enable registered account
        return account;
    }

    /**
     * Method to get a list of all accounts currently in the database
     * @return A List of all accounts
     */
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    /**
     * Method to get a specific account by their ID
     * @param id - The ID of the account to get
     * @return The Account object that has been retrieved
     */
    public Account getAccountById(Long id){
        // find user with provided id or throw exception
        return accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
    }

    /**
     * Method to delete a specific account by their ID
     * @param id - The ID of the account to delete
     * @return The Account object that has been deleted
     */
    public Account deleteAccount(Long id){
        Account accountToDelete = getAccountById(id);
        accountRepository.delete(accountToDelete);
        return accountToDelete;
    }

    /**
     * Method to update a specific account's info and save it to the database
     * @param id - The ID of the account to update
     * @param account - The Account object to update
     * @return The Account object that has just been updated
     */
    public Account updateAccount(Long id, Account account){
        Account accountToUpdate = getAccountById(id);

        boolean accountExists = accountRepository.findByEmail(account.getEmail()).isPresent();
        // if account with provided email already exist then throw exception
        if (accountExists && !accountRepository.findByEmail(account.getEmail()).equals(Optional.of(accountToUpdate))){
            throw new EmailAlreadyTakenException(account.getEmail());
        }

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(account.getEmail());
        // if provided email has an invalid format then throw exception
        if (!matcher.find()){
            throw new InvalidEmailFormatException(account.getEmail());
        }

        Matcher phoneMatcher = VALID_PHONE_NUMBER_REGEX.matcher(account.getPhone());
        // if provided phone number has an invalid format then throw exception
        if (!phoneMatcher.find()){
            throw new InvalidPhoneFormatException(account.getPhone());
        }
        // update account fields with newly provided information
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setAccAddress(account.getAccAddress());
        accountToUpdate.setPhone(account.getPhone());
        accountToUpdate.setEmail(account.getEmail());

        accountRepository.save(accountToUpdate);
        return accountToUpdate;
    }

    /**
     * Method to change the password of a specific account
     * @param id - The ID of the account to change password
     * @param password - The new password to be changed
     * @return The Account object that has just been updated
     */
    public Account changePassword(Long id, String password){
        Account accountToUpdate = getAccountById(id);
        String encodedPassword = bCryptPasswordEncoder.encode(password); // hash newly provided password
        accountToUpdate.setPassword(encodedPassword);
        accountRepository.save(accountToUpdate); // update new password into database
        return accountToUpdate;
    }

    /**
     * Method to change a specific account's role (USER or ADMIN)
     * @param id - The ID of the account to change role
     * @param accountRole - The new role that will be given to the account
     * @return The Account object that has just been updated
     */
    public Account setAccountRole(Long id, AccountRole accountRole){
        Account accountToUpdate = getAccountById(id);
        //Set the role of the account to the new role provided
        accountToUpdate.setAccountRole(accountRole);
        accountRepository.save(accountToUpdate);
        return accountToUpdate;
    }

}
