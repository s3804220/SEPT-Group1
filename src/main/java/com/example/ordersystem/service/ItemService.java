package com.example.ordersystem.service;

import com.example.ordersystem.exception.item.InvalidItemDescriptionException;
import com.example.ordersystem.exception.item.InvalidItemNameException;
import com.example.ordersystem.exception.item.InvalidItemPriceException;
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

/**
 * This class is the service layer for performing CRUD operations on Items
 */
@Transactional
@Service
public class ItemService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;

    /**
     * Method to save an item into the database
     * @param item - The Item object to be saved
     * @return The ID of the item that has just been saved
     */
    public Long saveItem(Item item){
        //Check the name of the item to make sure it's not empty
        if(item.getItemName().equals("")){
            throw new InvalidItemNameException(item.getItemName());
        }
        //Check the description of the item to make sure it's not empty
        if(item.getItemDescription().equals("")){
            throw new InvalidItemDescriptionException(item.getItemDescription());
        }
        //Check the price of the item to make sure it's a positive number
        if(item.getItemPrice().signum()<=0 ){
            throw new InvalidItemPriceException(item.getItemPrice().toString());
        }
        //After all checks, save the item into database
        Item newItem = itemRepository.save(item);
        return newItem.getId();
    }

    /**
     * Method to get an item by ID if it exists in the database
     * @param id - The ID of the item to get
     * @return The Optional object which may contain the item to get
     */
    public Optional<Item> getItem(Long id){
        return itemRepository.findById(id);
    }

    /**
     * Method to get a list of all items currently in the database
     * @return A List of all items found
     */
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    /**
     * Method to get a list of all items sorted by their ID
     * @return A List of all items found, sorted by their ID ascending
     */
    public List<Item> getAllItemsSortedId(){
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Method to get the string of an item's image file names
     * @param id - The ID of the item to get images
     * @return The String of that item's image file names
     */
    public String getItemImages(Long id){
        Item itemToGet = getItem(id).get();
        return itemToGet.getItemImage();
    }

    /**
     * Method to delete a specific item by ID
     * @param id - The ID of the item to be deleted
     */
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }

    /**
     * Method to change an item's availability status
     * @param id - The ID of the item to be updated
     */
    public void changeAvailability(Long id){
        Item item = getItem(id).get();
        //Change the item's availability status to the opposite of what it currently is
        item.setAvailability(!item.isAvailability());
        if(!item.isAvailability()){
            //If the item becomes unavailable, delete it from all users' carts
            //so the users cannot check out any cart with that item
            Set<Cart> itemCarts = item.getCarts();
            if(itemCarts!=null){
                cartRepository.deleteAll(itemCarts);
            }
        }
    }

    /**
     * Method to get the number of items of a category which satisfy the conditions
     * @param filterField - The field to filter the items by
     * @param searchField - The field to search the items by
     * @return The int number of items which satisfy the conditions
     */
    public int findNumOfSearchedItems(String filterField, String searchField) {
        String queryStr = "";

        if(!filterField.equals("All")) {
            queryStr = "select count(b) from Item b";

        } else {
            queryStr = "select count(*) from Item b";

        }
        queryStr += getQueryString(filterField, searchField);

        return ((Number) em.createQuery(queryStr).getSingleResult()).intValue();
    }

    /**
     * Method to get a list of filtered, searched by keyword and sorted items for a page
     * @param startIndex - The starting index to for the items
     * @param pageSize - The size of a single page
     * @param filterField - The field by which the items will be filtered
     * @param sortField - The field by which the items will be sorted
     * @param searchField - The field by which the items will be searched
     * @return The List of all items which is organized and ordered according to the conditions
     */
    public List<Item> findListPaging(int startIndex, int pageSize, String filterField, String sortField, String searchField) {

        String direction = "";
        String queryStr = "select b from Item b" + getQueryString(filterField, searchField);

        // Sort by different fields
        if (sortField.equals("name")) sortField = "itemName";
        if (sortField.equals("priceHTL")) {
            sortField = "itemPrice";
            direction = " desc";
        } else if(sortField.equals("priceLTH")) {
            sortField = "itemPrice";
            direction = " asc";
        }
        queryStr += " order by b." + sortField + direction;

        return em.createQuery(queryStr, Item.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Method to get the string to query items in the database
     * @param filterField - The field by which items will be filtered
     * @param searchField - The field by which items will be searched
     * @return The query String to query the database
     */
    public String getQueryString(String filterField, String searchField) {
        String queryStr = "";

        // Set query for filter field
        if(!filterField.equals("All")) {
            queryStr += " where b.category like '" + filterField + "'";
            if(!searchField.equals(""))
                queryStr +=" and ";
        }

        // Set query for search field
        if(!searchField.equals("")) {
            if(filterField.equals("All")) queryStr += " where";
            queryStr += " lower(b.itemName) like lower('%" + searchField + "%')";
        }

        return queryStr;
    }
}
