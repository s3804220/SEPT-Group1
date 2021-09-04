package com.example.ordersystem.service;

import com.example.ordersystem.model.*;
import com.example.ordersystem.repository.*;
import com.example.ordersystem.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    // Update/Create cart and return the amount of items in the cart
    public int addItem(
//            Cart cart
            Long itemId, int amount, Account user
    ){
//        Cart newCart = cartRepository.save(cart);

        int addedAmount = amount;
        Item item = itemRepository.findById(itemId).get();
        Cart cart = cartRepository.findByAccountAndItem(user, item);

        if(cart != null) {
            addedAmount = cart.getAmount() + addedAmount;
            cart.setAmount(addedAmount);
        } else {
            cart = new Cart();
            cart.setAmount(amount);
            cart.setAccount(user);
            cart.setItem(item);
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
    public int updateAmount(int amount, Long itemId, Account user) {

        cartRepository.updateAmount(amount, itemId, user.getId());
        Item item = itemRepository.findById(itemId).get();

        return (item.getItemPrice().intValue() * amount); // Pass new amount * 'the number of items'
    }
}
