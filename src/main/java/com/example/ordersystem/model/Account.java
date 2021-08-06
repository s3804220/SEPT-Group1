package com.example.ordersystem.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name="Account")
public class Account implements UserDetails {
    @SequenceGenerator(name ="account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String accAddress;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    private Boolean locked = false;
    private Boolean enabled = false;

    public Account(String firstName, String lastName, String accAddress, String phone, String email,
                   String password, AccountRole accountRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accAddress = accAddress;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.accountRole = accountRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(accountRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccAddress() {
        return accAddress;
    }

    public void setAccAddress(String accAddress) {
        this.accAddress = accAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountRole accountRole) {
        this.accountRole = accountRole;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accAddress='" + accAddress + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountRole=" + accountRole +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}
