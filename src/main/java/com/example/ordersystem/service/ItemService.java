package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ItemService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

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

    //Delete an item by ID
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
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
