package com.example.ordersystem.service;

import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class ItemService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;

    //Save an item in the database and return its ID (for usages if needed)
    public Long saveItem(Item item){
        Item newItem = itemRepository.save(item);
        return newItem.getId();
    }

    //Get an item by ID if it exists in the database
    public Optional<Item> getItem(Long id){
        return itemRepository.findById(id);
    }

    //Get a list of all items currently in the database
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    //Get a list of all items sorted by ID
    public List<Item> getAllItemsSortedId(){
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    //Delete an item by ID
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }

    public void changeAvailability(Long id){
        Item item = getItem(id).get();
        //Change the item's availability to the opposite of what it currently is
        item.setAvailability(!item.isAvailability());
        if(!item.isAvailability()){
            //If the item becomes unavailable, delete it from all users' carts
            //so the users cannot check out any cart with that item
            Set<Cart> itemCarts = item.getCarts();
            cartRepository.deleteAll(itemCarts);
        }
    }

    public int findTotal() {
        return ((Number) em.createQuery("select count(*) from Item")
                .getSingleResult()).intValue();
    }

    public List<Item> findListPaging(int startIndex, int pageSize) {
        return em.createQuery("select b from Item b", Item.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
