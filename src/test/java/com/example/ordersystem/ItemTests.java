package com.example.ordersystem;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.ItemRepository;
import com.example.ordersystem.service.ItemService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements="ALTER TABLE items ALTER COLUMN id RESTART WITH 1"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements="ALTER TABLE items ALTER COLUMN id RESTART WITH 1")
})
@SpringBootTest
public class ItemTests {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    public void init(){
        itemRepository.deleteAll();
        //Add new item before each test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal(3.99));
        itemService.saveItem(newItem);
    }

    @AfterEach
    public void clearDatabase(){
        itemRepository.deleteAll();
    }

    @Test
    public void addItemTests(){
        assertEquals(1,itemService.getAllItems().size());
        assertEquals("Hot dog", itemService.getItem(1L).get().getItemName());

        //Add another item
        Item newItem2 = new Item("Pie","Yummy pie","food.png|image.jpg",new BigDecimal(7));
        itemService.saveItem(newItem2);
        assertEquals(2,itemService.getAllItems().size());
        assertEquals(newItem2.getItemDescription(), itemService.getItem(2L).get().getItemDescription());
    }

    @Test
    public void updateItemTests(){
        Item itemToUpdate = itemService.getItem(1L).get();
        //Update the above item's name
        itemToUpdate.setItemName("Super hot dog");
        itemService.saveItem(itemToUpdate);
        assertEquals("Super hot dog", itemService.getItem(1L).get().getItemName());
    }

    @Test
    public void getItemByIdTests(){
        Item itemToGet, itemToGet2, itemToGet3;
        //Get an item which is present in the database
        if(itemService.getItem(1L).isPresent()){
            itemToGet = itemService.getItem(1L).get();
        }else {
            itemToGet = null;
        }
        assertNotNull(itemToGet);

        //Get an item which is not present in the database
        if(itemService.getItem(3L).isPresent()){
            itemToGet2 = itemService.getItem(3L).get();
        }else {
            itemToGet2 = null;
        }
        assertNull(itemToGet2);

        //Get an item which is not present in the database and have a negative ID
        if(itemService.getItem(-1L).isPresent()){
            itemToGet3 = itemService.getItem(-1L).get();
        }else {
            itemToGet3 = null;
        }
        assertNull(itemToGet3);
    }

    @Test
    public void deleteItemTests(){
        //Delete item then try to get it by ID
        itemService.deleteItem(1L);

        Item itemToGet;
        if(itemService.getItem(1L).isPresent()){
            itemToGet = itemService.getItem(1L).get();
        }else {
            itemToGet = null;
        }
        assertNull(itemToGet);
    }

    @Test
    public void getItemsListTests(){
        //Add two more items
        Item newItem2 = new Item("Pie","Yummy pie","food.png|image.jpg",new BigDecimal(7));
        itemService.saveItem(newItem2);
        Item newItem3 = new Item("Chocolate cake","Sweet and dark","choco.png",new BigDecimal(12));
        itemService.saveItem(newItem3);
        //Test that the list reflects the correct size
        assertEquals(3,itemService.getAllItems().size());
        //Delete and test that the change is reflected in the list
        itemService.deleteItem(1L);
        assertEquals(2,itemService.getAllItems().size());
        itemRepository.deleteAll();
        assertEquals(0,itemService.getAllItems().size());
    }
}
