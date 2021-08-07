package com.example.ordersystem.security.config;

import com.example.ordersystem.model.AccountRole;
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
                .antMatchers("/registration/**").permitAll()
                .antMatchers("/shop/**").permitAll()
                .antMatchers("/shop-details/**").permitAll()
                .antMatchers("/item-form.html").hasAuthority("ADMIN")
                .antMatchers("/item-list.html").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .defaultSuccessUrl("/user", true)
        ;
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

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/fonts/**", "/img/**", "/js/**", "/sass/**", "/Source/**");
    }
}
