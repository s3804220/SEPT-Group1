package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Shop;
import com.example.ordersystem.model.Student;
import com.example.ordersystem.repository.*;
import com.example.ordersystem.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Transactional
@Service
public class CartService implements CustomCartRepository {

    @PersistenceContext
    private EntityManager em;

    private CartRepository cartRepository;
    private ShopRepository shopRepository;


    @Autowired
    public CartService(CartRepository cartRepository, ShopRepository shopRepository) {
        this.cartRepository = cartRepository;
        this.shopRepository = shopRepository;
    }



    public int addShop(
//            Cart cart
            Long shopId, int amount, Account user
    ){
//        Cart newCart = cartRepository.save(cart);

        int addedAmount = amount;
        Shop shop = shopRepository.findById(shopId).get();
        Cart cart = cartRepository.findByAccountAndShop(user, shop);

        if(cart != null) {
            addedAmount = cart.getAmount() + addedAmount;
            cart.setAmount(addedAmount);
        } else {
            cart = new Cart();
            cart.setAmount(amount);
            cart.setAccount(user);
            cart.setShop(shop);
        }

        cartRepository.save(cart);
        return addedAmount;

    }


    public Cart getCart(Long id){
        return cartRepository.getById(id);
    }


    public List<Cart> getAllCarts(Account account){
        return cartRepository.findByAccount(account);
    }

    public void deleteCart(Long id){
        cartRepository.delete(getCart(id));
    }




//    @Override
    public Cart findCartById(Long id) {
        return cartRepository.findById(id).orElse(new Cart());
    }





    // Update the amount of a cart item
    public int updateAmount(int amount, Long shopId, Account user) {

        cartRepository.updateAmount(amount, shopId, user.getId());
        Shop shop = shopRepository.findShopById(shopId);

        return (shop.getPrice().intValue() * amount); // Pass new amount * 'the number of items'
    }
}
