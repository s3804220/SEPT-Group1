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

    /**
     * Mapping of the endpoint to get all items in the database
     * @return A List of all items in the database
     */
    @GetMapping(path = "/items")
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    /**
     * Mapping of the endpoint to add a new item into the database
     * @param item - The Item object to be added
     * @return The added Item's ID as a response
     */
    @PostMapping(path = "/items")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    /**
     * Mapping of the endpoint to update an item in the database
     * @param item - The Item object to be updated
     * @return The updated Item's ID as a response
     */
    @PutMapping(path = "/items")
    public ResponseEntity<String> updateItem(@RequestBody Item item){
        Long newID= itemService.saveItem(item);
        String IDstring = newID.toString();
        return ResponseEntity.status(HttpStatus.OK).body(IDstring);
    }

    /**
     * Mapping of the endpoint to delete an item from the database by ID
     * @param id - The ID of the item to be deleted
     */
    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
    }

    /**
     * Mapping of the endpoint to get an item from the database by ID
     * @param id - The ID of the item to get
     * @return An Optional object which may contain the item to get
     */
    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Long id){
        return itemService.getItem(id);
    }

    /**
     * Mapping of the endpoint to get all image names of an item by ID
     * @param id - The ID of the item whose images will be retrieved
     * @return A response entity which contains the String of all image names
     */
    @GetMapping(path = "/items/images/{id}")
    public ResponseEntity<String> getItemImages(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemImages(id));
    }

}
