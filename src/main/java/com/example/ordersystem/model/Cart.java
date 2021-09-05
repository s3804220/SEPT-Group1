package com.example.ordersystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JsonBackReference(value = "cart-item")
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JsonBackReference(value="account-cart")
    @JoinColumn(name = "account_id")
    private Account account;

    private int amount;

    @Transient
    public float getSmallSum() {
        return  this.item.getItemPrice().floatValue() * amount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
