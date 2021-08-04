package com.example.ordersystem.controller;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping(path = "/items")
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    //Add a new item to the database and return its ID as a response for checking and later usage
    @PostMapping(path = "/items")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    //Update an item in the database and return its ID as a response for checking and later usage
    @PutMapping(path = "/items")
    public ResponseEntity<String> updateItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
    }

    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Long id){
        return itemService.getItem(id);
    }
}
