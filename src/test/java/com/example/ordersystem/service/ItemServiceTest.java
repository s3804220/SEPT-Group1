package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.ItemRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Reset auto-generated ID back to 1 for accurate testing environment
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements="ALTER TABLE items ALTER COLUMN id RESTART WITH 1"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements="ALTER TABLE items ALTER COLUMN id RESTART WITH 1")
})
@SpringBootTest
public class ItemServiceTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    public void init(){
        //Delete all items in the database before testing
        itemRepository.deleteAll();
        //Add new item before each test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal("3.99"));
        itemService.saveItem(newItem);
    }

    @AfterEach
    public void clearDatabase(){
        itemRepository.deleteAll();
    }

    @Test
    public void addItemTests(){
        //Test that item has been added correctly and it exists in the item list
        assertEquals(1,itemService.getAllItems().size());
        assertEquals("Hot dog", itemService.getItem(1L).get().getItemName());

        //Add another item with special characters for a similar test
        Item newItem2 = new Item("Pie","Yummy pie!!!!&%%%w0w=*","food.png|image.jpg",new BigDecimal("7"));
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
        //Test that the item name is correctly updated
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
        assertEquals(1L, itemToGet.getId().longValue());

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

        //Try to delete non-existent item
        assertThrows(EmptyResultDataAccessException.class, () -> itemService.deleteItem(9L));
    }

    @Test
    public void getItemsListTests(){
        //Add two more items
        Item newItem2 = new Item("Pie","Yummy pie","food.png|image.jpg",new BigDecimal("7"));
        itemService.saveItem(newItem2);
        Item newItem3 = new Item("Chocolate cake","Sweet and dark","choco.png",new BigDecimal("12"));
        itemService.saveItem(newItem3);

        //Test that the list reflects the correct size
        assertEquals(3,itemService.getAllItems().size());
        //Delete and test that the change is reflected in the list
        itemService.deleteItem(1L);

        //Check that the list contains correct information
        assertEquals(2,itemService.getAllItems().size());
        assertEquals("Pie",itemService.getAllItems().get(0).getItemName());
        assertEquals("Yummy pie",itemService.getAllItems().get(0).getItemDescription());
        assertEquals("food.png|image.jpg",itemService.getAllItems().get(0).getItemImage());
        assertEquals(new BigDecimal("7.00"),itemService.getAllItems().get(0).getItemPrice());

        //Test that the list is updated correctly if there is no item in the database
        itemRepository.deleteAll();
        assertEquals(0,itemService.getAllItems().size());
    }

    @Test
    public void findTotalTests() {
        Item testItem1 = new Item("testCake1", "Frist test", "product-1.jpg", new BigDecimal("11.00"));
        Item testItem2 = new Item("testCake2","Last test", "product-2.jpg", new BigDecimal("8.00"));

        itemService.saveItem(testItem1);
        itemService.saveItem(testItem2);

        int amount = 2;
        int num = itemService.findTotal();

        assertTrue(amount <= itemService.findTotal());
        assertEquals(num, itemService.findTotal());
    }

    @Test
    public void findListPaging() {
        Item testItem1 = new Item("testCake1", "First test", "product-1.jpg", new BigDecimal("11.00"));
        Item testItem2 = new Item("testCake2","Last test", "product-2.jpg", new BigDecimal("8.00"));

        itemService.saveItem(testItem1);
        itemService.saveItem(testItem2);

        List<Item> itemList = itemService.findListPaging(0, 12);

        List<Item> itemList2 = itemService.getAllItems();

        int amount = itemList2.size()-1;

        assertEquals(itemList2.get(0).getItemName(), itemList.get(0).getItemName());
        assertEquals(itemList2.get(0).getItemPrice(), itemList.get(0).getItemPrice());
        assertEquals(itemList2.get(0).getItemDescription(), itemList.get(0).getItemDescription());

        assertEquals(itemList2.get(amount).getItemName(), itemList.get(amount).getItemName());
        assertEquals(itemList2.get(amount).getItemPrice(), itemList.get(amount).getItemPrice());
        assertEquals(itemList2.get(amount).getItemDescription(), itemList.get(amount).getItemDescription());
    }
}
