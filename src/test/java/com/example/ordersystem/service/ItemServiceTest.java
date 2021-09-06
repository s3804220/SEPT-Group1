package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Delete table data and reset auto-generated ID back to 1 for accurate testing environment
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "TRUNCATE items RESTART IDENTITY CASCADE"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "TRUNCATE items RESTART IDENTITY CASCADE")

})
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class ItemServiceTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    public void init() {
        //Delete all items in the database before testing
        itemRepository.deleteAll();
        //Add new item before each test case
        Item newItem = new Item("Hot dog", "Very hot", "dog.jpg", new BigDecimal("3.99"), "Hotdog", true);

        itemService.saveItem(newItem);
    }

    @AfterEach
    public void clearDatabase() {
        itemRepository.deleteAll();
    }

    @Test
    public void addItemTests() {
        //Test that item has been added correctly and it exists in the item list
        assertEquals(1, itemService.getAllItems().size());
        assertEquals("Hot dog", itemService.getItem(1L).get().getItemName());
        assertEquals("Hotdog", itemService.getItem(1L).get().getCategory());

        //Add another item with special characters for a similar test
        Item newItem2 = new Item("Pie", "Yummy pie!!!!&%%%w0w=*", "food.png|image.jpg", new BigDecimal("7"), "Pie", true);
        itemService.saveItem(newItem2);
        assertEquals(2, itemService.getAllItems().size());
        assertEquals(newItem2.getItemDescription(), itemService.getItem(2L).get().getItemDescription());
        assertEquals("Pie", itemService.getItem(2L).get().getCategory());
    }

    @Test
    public void updateItemTests() {
        Item itemToUpdate = itemService.getItem(1L).get();

        //Update the above item's name
        itemToUpdate.setItemName("Super hot dog");
        itemService.saveItem(itemToUpdate);
        //Test that the item name is correctly updated
        assertEquals("Super hot dog", itemService.getItem(1L).get().getItemName());
    }

    @Test
    public void getItemByIdTests() {
        Item itemToGet, itemToGet2, itemToGet3;

        //Get an item which is present in the database
        if (itemService.getItem(1L).isPresent()) {
            itemToGet = itemService.getItem(1L).get();
        } else {
            itemToGet = null;
        }
        assertNotNull(itemToGet);
        assertEquals(1L, itemToGet.getId().longValue());

        //Get an item which is not present in the database
        if (itemService.getItem(3L).isPresent()) {
            itemToGet2 = itemService.getItem(3L).get();
        } else {
            itemToGet2 = null;
        }
        assertNull(itemToGet2);

        //Get an item which is not present in the database and have a negative ID
        if (itemService.getItem(-1L).isPresent()) {
            itemToGet3 = itemService.getItem(-1L).get();
        } else {
            itemToGet3 = null;
        }
        assertNull(itemToGet3);
    }

    @Test
    public void deleteItemTests() {
        //Delete item then try to get it by ID
        itemService.deleteItem(1L);

        Item itemToGet;
        if (itemService.getItem(1L).isPresent()) {
            itemToGet = itemService.getItem(1L).get();
        } else {
            itemToGet = null;
        }
        assertNull(itemToGet);

        //Try to delete non-existent item
        assertThrows(EmptyResultDataAccessException.class, () -> itemService.deleteItem(9L));
    }

    @Test
    public void getItemsListTests() {
        //Add two more items
        Item newItem2 = new Item("Pie", "Yummy pie", "food.png|image.jpg", new BigDecimal("7"), "Pie", true);
        itemService.saveItem(newItem2);
        Item newItem3 = new Item("Chocolate cake", "Sweet and dark", "choco.png", new BigDecimal("12"), "Cake", true);
        itemService.saveItem(newItem3);

        //Test that the list reflects the correct size
        assertEquals(3, itemService.getAllItems().size());
        //Delete and test that the change is reflected in the list
        itemService.deleteItem(1L);

        //Check that the list contains correct information
        assertEquals(2, itemService.getAllItems().size());
        assertEquals("Pie", itemService.getAllItems().get(0).getItemName());
        assertEquals("Yummy pie", itemService.getAllItems().get(0).getItemDescription());
        assertEquals("food.png|image.jpg", itemService.getAllItems().get(0).getItemImage());
        assertEquals(new BigDecimal("7"), itemService.getAllItems().get(0).getItemPrice());
        assertEquals("Pie", itemService.getAllItems().get(0).getCategory());
        assertTrue(itemService.getAllItems().get(0).isAvailability());

        //Test that the list is updated correctly if there is no item in the database
        itemRepository.deleteAll();
        assertEquals(0, itemService.getAllItems().size());
    }


    @Test
    public void getItemImagesTests() {
        //Check that the item image names can be correctly retrieved
        assertEquals("dog.jpg", itemService.getItemImages(1L));
    }

    @Test
    public void changeAvailabilityTests() {
        //Test that the item availability be changed correctly
        //Change the available status of item 1 to false
        itemService.changeAvailability(1L);
        assertFalse(itemService.getItem(1L).get().isAvailability());
        //Change the available status of item 1 back to true
        itemService.changeAvailability(1L);
        assertTrue(itemService.getItem(1L).get().isAvailability());
    }

    @Test
    public void findNumOfSearchedItemsTests() {
        // Add more items before conducting test
        Item newItem2 = new Item("Pie", "Yummy pie", "food.png|image.jpg", new BigDecimal("7"), "Pie", true);
        Item newItem3 = new Item("Hot chocolate cake", "Sweet and dark", "choco.png", new BigDecimal("12"), "Cake", true);
        itemService.saveItem(newItem2);
        itemService.saveItem(newItem3);


        // Set filter and search conditions to test
        String filterField = "All";
        String searchField = "hot";
        //Use the filter and search method to get the number of items matching the conditions
        int numOfItems = itemService.findNumOfSearchedItems(filterField, searchField);
        List<Item> itemListForTesting = itemService.getAllItems();
        int numOfItemsInItemList = 0;

        for (Item item : itemListForTesting) {
            if (item.getItemName().equalsIgnoreCase(searchField)) numOfItemsInItemList++;
            else {
                if (item.getCategory().equals(filterField))
                    if (item.getItemName().equalsIgnoreCase(searchField)) numOfItemsInItemList++;
            }
        }

        for (Item item : itemListForTesting) {
            if (item.getItemName().equalsIgnoreCase(searchField)) {
                numOfItemsInItemList++;
            }
        }

        //Assert the number of items returned matches the expected number
        assertEquals(2, numOfItems);
    }


    @Test
    public void findListPagingTests() {
        // Add more items before conducting test
        Item newItem2 = new Item("Pie", "Yummy pie", "food.png|image.jpg", new BigDecimal("7"), "Pie", true);
        Item newItem3 = new Item("Hot chocolate cake", "Sweet and dark", "choco.png", new BigDecimal("12"), "Cake", true);
        itemService.saveItem(newItem2);
        itemService.saveItem(newItem3);

        // Set filter, sort and search conditions for testing
        String filterField = "All";
        String sortField = "priceHTL";
        String searchField = "hot";
        //Get the item list based on the conditions above
        List<Item> itemList = itemService.findListPaging(0, 12, filterField, sortField, searchField);
        List<Item> fullItemListForTesting = itemService.getAllItems();
        List<Item> tempItemListForTesting = new ArrayList<>();
        List<Item> itemListForTesting = new ArrayList<>();


        for (Item item : fullItemListForTesting) {
            // Apply search condition
            if (item.getItemName().toLowerCase().contains(searchField.toLowerCase())) {
                // Apply filter condition
                if (filterField.equals("All")) {
                    tempItemListForTesting.add(item);
                } else {
                    if (item.getCategory().equals(filterField)) {
                        tempItemListForTesting.add(item);
                    }
                }
            }
        }
        // Apply sorting condition
        switch (sortField) {
            case "id":
                tempItemListForTesting.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
                break;

            case "name":
                tempItemListForTesting.sort((o1, o2) -> o1.getItemName().compareTo(o2.getItemName()));
                break;

            case "priceHTL":
                tempItemListForTesting.sort((o1, o2) -> o2.getItemPrice().compareTo(o1.getItemPrice())); //descending order
                break;

            case "priceLTH":
                tempItemListForTesting.sort((o1, o2) -> o1.getItemPrice().compareTo(o2.getItemPrice())); //ascending order
                break;
        }

        int loopSize = 0;
        while (loopSize < tempItemListForTesting.size() && loopSize < 12) {
            itemListForTesting.add(tempItemListForTesting.get(loopSize));
            loopSize++;
        }
        //Assert that the items retrieved have the correct information
        assertEquals(itemListForTesting.get(0).getId(), itemList.get(0).getId());
        assertEquals(itemListForTesting.get(0).getItemName(), itemList.get(0).getItemName());
        assertEquals(itemListForTesting.size(), itemList.size());

    }
}
