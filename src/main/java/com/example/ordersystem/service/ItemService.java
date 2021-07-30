package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Long saveItem(Item item){
        Item newItem = itemRepository.save(item);
        return newItem.getId();
    }

    public Item getItem(Long id){
        return itemRepository.getById(id);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }
}
