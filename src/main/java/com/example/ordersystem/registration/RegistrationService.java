package com.example.ordersystem.registration;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.AccountRole;
import com.example.ordersystem.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final AccountService accountService;
    private static final String INVALID_EMAIL_ADDRESS = "This email format is invalid";

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException(INVALID_EMAIL_ADDRESS);
        }
        return accountService.signUpAccount(
                new Account(
                       request.getFirstName(),
                       request.getLastName(),
                        request.getAccAddress(),
                        request.getPhone(),
                        request.getEmail(),
                        request.getPassword(),
                        AccountRole.USER
                )
        );
    }
}
