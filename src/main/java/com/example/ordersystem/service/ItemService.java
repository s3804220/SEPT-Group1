package com.example.ordersystem.service;

import com.example.ordersystem.exception.item.InvalidItemDescriptionException;
import com.example.ordersystem.exception.item.InvalidItemNameException;
import com.example.ordersystem.exception.item.InvalidItemPriceException;
import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.Pagination;
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

    //Function to save an item in the database and return its ID (for later usages when needed)
    public Long saveItem(Item item){
        if(item.getItemName().equals("")){
            throw new InvalidItemNameException(item.getItemName());
        }
        if(item.getItemDescription().equals("")){
            throw new InvalidItemDescriptionException(item.getItemDescription());
        }
        if(item.getItemPrice().signum()<=0 ){
            throw new InvalidItemPriceException(item.getItemPrice().toString());
        }
        Item newItem = itemRepository.save(item);
        return newItem.getId();
    }

    //Function to get an item by ID if it exists in the database
    public Optional<Item> getItem(Long id){
        return itemRepository.findById(id);
    }

    //Function to get a list of all items currently in the database
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    //Function to get a list of all items sorted by ID
    public List<Item> getAllItemsSortedId(){
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    //Function to get the string of an item's image file names
    public String getItemImages(Long id){
        Item itemToGet = getItem(id).get();
        return itemToGet.getItemImage();
    }

    //Function to delete a specific item by ID
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }

    //Function to change an item's availability status
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


    // Get the number of items of a category (filtered)
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
    

    // Get a list of filtered, sorted items for a page
    public List<Item> findListPaging(int startIndex, int pageSize, String filterField, String sortField, String searchField) {

        String direction = "";

        String queryStr = "select b from Item b" + getQueryString(filterField, searchField);
        // Sort Field
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


    public String getQueryString(String filterField, String searchField) {
        String queryStr = "";

//         Filter Field
        if(!filterField.equals("All")) {
            queryStr += " where b.category like '" + filterField + "'";
            if(!searchField.equals(""))
                queryStr +=" and ";
        }

        // Search Field
        if(!searchField.equals("")) {
            if(filterField.equals("All")) queryStr += " where";
            queryStr += " lower(b.itemName) like lower('%" + searchField + "%')";
        }

        return queryStr;
    }
}
