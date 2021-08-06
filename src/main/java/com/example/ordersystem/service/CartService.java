package com.example.ordersystem.service;

import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Student;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.CustomCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ordersystem.repository.StudentRepository;

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


    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public void addCart(Cart cart
//            Long shopId, int amount, String userId
    ){
        Cart newCart = cartRepository.save(cart);
//        int addedAmount = amount;
//
//        Cart cart = cartRepository.findByUserIdAndShopId(userId, shopId);
//
//        if(cart != null) {
//            addedAmount = cart.getAmount() + addedAmount;
//            cart.setAmount(addedAmount);
//        } else {
//            cart = new Cart();
//            cart.setAmount(amount);
//            cart.setUserId(userId);
//            cart.setShopId(shopId);
//        }
//
//        cartRepository.save(cart);
//        return addedAmount;

//        em.createNativeQuery("INSERT INTO Cart(id, userId, userName, shopId, shopName, shopPrice, image, amount) " +
//                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
//                .setParameter(1, cart.getId())
//                .setParameter(2, cart.getUserId())
//                .setParameter(3, cart.getUserName())
//                .setParameter(4, cart.getShopId())
//                .setParameter(5, cart.getShopName())
//                .setParameter(6, cart.getShopPrice())
//                .setParameter(7, cart.getImage())
//                .setParameter(8, cart.getAmount())
//                .executeUpdate();
    }


    public Cart getCart(Long id){
        return cartRepository.getById(id);
    }


    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

    public void deleteCart(Long id){
        cartRepository.delete(getCart(id));
    }


    public void modifyCart(Cart cart) {
        em.createQuery("update Cart c set c.amount = :amount where" +
//                " c.userId = :userId and" +
                " c.shopId = :shopId")
                .setParameter("amount", cart.getAmount())
//                .setParameter("userId", cart.getUserId())
                .setParameter("shopId", cart.getShopId())
                .executeUpdate();
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
//    @Override
    public float updateCart(Cart cart) {
        em.createQuery("update Cart c set c.amount = :amount where" +
//                " c.userId = :userId and" +
                " c.shopId = :shopId")
                .setParameter("amount", cart.getAmount())
//                .setParameter("userId", cart.getUserId())
                .setParameter("shopId", cart.getShopId())
                .executeUpdate();

        return (float) (cart.getShopPrice().intValue() * cart.getAmount());
    }
}
