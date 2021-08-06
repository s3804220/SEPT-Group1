package com.example.ordersystem.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    @NotNull
    private String userId;

    @Column
    private String userName;

//    @ManyToOne
//    @JoinColumn(name = "shop_id", nullable = false)
//    private Shop shop;

    @Column
    private Long shopId;

    @Column
    private String shopName;

    @Column
    private BigDecimal shopPrice; // Sum

    @Column
    private String image;

    @Column
    @ColumnDefault("0")
    private int amount;

    public Cart() {
    }

    public Cart(Long id, String userId, String userName, Long shopId, String shopName, BigDecimal shopPrice, String image, int amount) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopPrice = shopPrice;
        this.image = image;
        this.amount = amount;
    }

    @Transient
    public float getShoptotal() {
        return this.getShopPrice().intValue() * amount;
    }

    public Long getCart_id() {
        return id;
    }

    public void setCart_id(Long cart_id) {
        this.id = cart_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(BigDecimal shopPrice) {
        this.shopPrice = shopPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
