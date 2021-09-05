package com.example.ordersystem.service;

import com.example.ordersystem.model.Account;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class is the service layer for performing CRUD operations on items in the Cart
 */

@Transactional
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    /**
     * Method to add an item to the cart of an account and return the number of that item in the cart
     * @param itemId - The ID of the item to be added
     * @param amount - The quantity of the item to be added
     * @param user - The user Account object to which the cart belongs
     * @return The quantity number of that item currently in the cart
     */
    public int addItem(Long itemId, int amount, Account user) {

        int addedAmount = amount;
        //Get the item and the user's cart
        Item item = itemRepository.findById(itemId).get();
        Cart cart = cartRepository.findByAccountAndItem(user, item);

        //If a cart exists in the database for that item and account, increase the quantity of the item in that cart
        // else, it means no cart record exists in the database for that item and account, then create a new cart and add the item
        if (cart != null) {
            addedAmount = cart.getAmount() + addedAmount;
            cart.setAmount(addedAmount);
        } else {
            cart = new Cart();
            cart.setAmount(amount);
            cart.setAccount(user);
            cart.setItem(item);
        }

        //Save the cart into database
        cartRepository.save(cart);
        return addedAmount;

    }

    /**
     * Method to get a user's cart by ID
     * @param id - The ID of the cart to get
     * @return The Cart object to get
     */
    public Cart getCart(Long id) {
        return cartRepository.getById(id);
    }

    /**
     * Method to get a list of all carts attached to a particular account
     * @param account - The Account to find the carts
     * @return The List of all carts attached to that Account
     */
    public List<Cart> getAllCarts(Account account) {
        return cartRepository.findByAccount(account);
    }

    /**
     * Method to delete a cart by ID
     * @param id - The ID of the cart to delete
     */
    public void deleteCart(Long id) {
        cartRepository.delete(getCart(id));
    }

    /**
     * Method to find a cart by ID
     * @param id - The ID of the cart to find
     * @return The Cart object to find, if it does not exist, create a new cart instead
     */
    public Cart findCartById(Long id) {
        return cartRepository.findById(id).orElse(new Cart());
    }

    /**
     * Method to update the quantity and subtotal price of a particular item in the user's cart
     * @param amount - The quantity of the item to update
     * @param itemId - The ID of the item to update
     * @param user - The user Account to which the cart belongs
     * @return The new subtotal price of that particular item in the user's cart
     */
    public int updateAmount(int amount, Long itemId, Account user) {

        cartRepository.updateAmount(amount, itemId, user.getId());
        Item item = itemRepository.findById(itemId).get();

        return (item.getItemPrice().intValue() * amount); // Pass new amount multiplied by 'the int price of items'
    }
}
