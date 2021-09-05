package com.example.ordersystem.controller;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This class is used for routing RESTful endpoints to perform CRUD on the shop items.
 */
@RestController
public class ItemRestController {
    @Autowired
    private ItemService itemService;

    //Map the endpoint to get all items in the database
    @GetMapping(path = "/items")
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    //Map the endpoint to add a new item into the database and return its ID as a response for checking and later usage
    @PostMapping(path = "/items")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    //Map the endpoint to update an item in the database and return its ID as a response for checking and later usage
    @PutMapping(path = "/items")
    public ResponseEntity<String> updateItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    //Map the endpoint to delete an item from the database by ID
    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
    }

    //Map the endpoint to get an item from the database by ID
    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Long id){
        return itemService.getItem(id);
    }

    //Map the endpoint to get all images of an item by ID
    @GetMapping(path = "/items/images/{id}")
    public ResponseEntity<String> getItemImages(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemImages(id));
    }

}
