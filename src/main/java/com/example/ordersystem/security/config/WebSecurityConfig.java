package com.example.ordersystem.security.config;

import com.example.ordersystem.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * This class configures the security layer of the web application
 * It sets the authority to view specific paths,
 * and other security measures such as XSS prevention and password encoding
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Configure path permission and redirect user to index page after login
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/registration/**","/login").permitAll()
                .antMatchers("/shop/**").permitAll()
                .antMatchers("/shop-details/**").permitAll()
                .antMatchers("/about").permitAll()
                .antMatchers("/contact").permitAll()
                .antMatchers("/admin-panel").hasAuthority("ADMIN")
                .antMatchers("/item-form").hasAuthority("ADMIN")
                .antMatchers("/item-list").hasAuthority("ADMIN")
                .antMatchers("/account-management/**").hasAuthority("ADMIN")
                .antMatchers("/orderlist/**").hasAuthority("ADMIN")
                .antMatchers("/shopping-cart/**").authenticated()
                .antMatchers("/checkout/**").authenticated()
                .antMatchers("/items/**","/item/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").authenticated()
                .antMatchers("/order-history/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler())
                .failureUrl("/login?error=true")
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied")
        ;
        http
                .headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");
    }

    // configure authentication provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    // Set password encoder to BCryptPasswordEncoder and set AccountService for UserDetailService
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(accountService);
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new MyCustomLoginSuccessHandler("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/fonts/**", "/img/**", "/js/**", "/sass/**", "/Source/**");
    }
}
