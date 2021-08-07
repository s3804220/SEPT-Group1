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

//        em.createNativeQuery("INSERT INTO Cart(userId, userName, shopId, shopName, shopPrice, image, amount) " +
//                                "VALUES(?, ?, ?, ?, ?, ?, ?)")
//                .setParameter(1, cart.getUserId())
//                .setParameter(2, cart.getUserName())
//                .setParameter(3, cart.getShopId())
//                .setParameter(4, cart.getShopName())
//                .setParameter(5, cart.getShopPrice())
//                .setParameter(6, cart.getImage())
//                .setParameter(7, cart.getAmount())
//                .executeUpdate();
    }


    public Cart getCart(Long id){
        return cartRepository.getById(id);
    }


//    public List<Cart> getAllCarts(){
//        return cartRepository.findAll();
//    }
    public List<Cart> getAllCarts(Account account){
        return cartRepository.findByAccount(account);
    }

    public void deleteCart(Long id){
        cartRepository.delete(getCart(id));
    }


    public void modifyCart(Cart cart) {
//        em.createQuery("update Cart c set c.amount = :amount where" +
////                " c.userId = :userId and" +
//                " c.shopId = :shopId")
//                .setParameter("amount", cart.getAmount())
////                .setParameter("userId", cart.getUserId())
//                .setParameter("shopId", cart.getShopId())
//                .executeUpdate();
    }

//    @Override
    public int sumPrice(String userId) {
//        return 9999999;
        return ((int) em.createQuery("select sum(c.price) from Cart as c "
//                +"where c.userId = :userId"
                )
//                .setParameter("userId", userId)
                .getSingleResult());
    }

//    @Override
    public Cart findCartById(Long id) {
        return cartRepository.findById(id).orElse(new Cart());
    }

//    @Override
    public int countCart(Long shopId, String userId) {
        return ((Number) em.createQuery("select count(*) from Cart as c where" +
//                " c.userId = :userId and" +
                " c.shopId = :shopId")
//                .setParameter("userId", userId)
                .setParameter("shopId", shopId)
                .getSingleResult()).intValue();
    }


    // Update the amount of a cart item
    public int updateAmount(int amount, Long shopId, Account user) {
//        em.createQuery("update Cart c set c.amount = :amount where c.account.id = :userId and c.shop.id = :shopId")
//                .setParameter("amount", amount)
//                .setParameter("userId", accountId)
//                .setParameter("shopId", shopId)
//                .executeUpdate();
//        System.out.println("Amount: "+amount +" - sName: "+shopRepository.findShopById(shopId).getName()+" user - "+user);
        cartRepository.updateAmount(amount, shopId, user.getId());
        Shop shop = shopRepository.findShopById(shopId);

        return (shop.getPrice().intValue() * amount); // Pass new amount * 'the number of items'
    }
}
