package com.example.ordersystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is the configuration class to set the password encoding algorithm
 */
@Configuration
public class PasswordEncoder {
    // Create bean for BCryptPasswordEncoder framework
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
