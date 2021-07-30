package com.example.ordersystem.controller;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping(path = "/items")
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @PostMapping(path = "/items")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    @PutMapping(path = "/items")
    public void updateItem(@RequestBody Item item){
        itemService.saveItem(item);
    }

    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
    }
}
