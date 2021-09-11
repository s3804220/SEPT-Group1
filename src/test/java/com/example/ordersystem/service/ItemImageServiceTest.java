package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.ItemImage;
import com.example.ordersystem.repository.ItemImageRepository;
import com.example.ordersystem.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class ItemImageServiceTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemImageService itemImageService;
    @Autowired
    private ItemImageRepository itemImageRepository;

    @BeforeEach
    public void init(){
        //Delete all items in the database before testing
        itemRepository.deleteAll();
    }

    @AfterEach
    public void clearDatabase(){
        itemRepository.deleteAll();
    }

    @Test
    public void saveItemImageTests(){
        //Add new item before conducting test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal("3.99"),"Hotdog",true);
        Long id = itemService.saveItem(newItem);
        try{
            //Create a mock image file from a byte array
            byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\", File.separator)));
            MockMultipartFile file = new MockMultipartFile("file", "honey-cake.jpg", "multipart/form-data", byteArray);
            //Save the image into database
            itemImageService.saveItemImage(id,file);
            //Test that the image has been saved by checking the size of the image list
            assertEquals(1,itemImageRepository.findAll().size());
            //Check that the ID of the item matches the expected value
            assertEquals(id, itemImageRepository.findAll().get(0).getItem().getId());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getItemImageByIdTests(){
        //Add new item before conducting test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal("3.99"),"Hotdog",true);
        Long id = itemService.saveItem(newItem);
        try{
            //Create a mock image file from a byte array
            byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\", File.separator)));
            MockMultipartFile file = new MockMultipartFile("file", "honey-cake.jpg", "multipart/form-data", byteArray);
            //Save the image into database
            itemImageService.saveItemImage(id,file);
            //Get the ID of the image to get
            Long imageId = itemImageRepository.findAll().get(0).getId();
            ItemImage itemImage = itemImageService.getItemImageById(imageId);
            //Check that the getID method returns the correct image
            assertEquals(id,itemImage.getItem().getId());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getAllItemImagesTests(){
        //Add new item before conducting test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal("3.99"),"Hotdog",true);
        Long id = itemService.saveItem(newItem);
        try{
            //Create a mock image file from a byte array
            byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\", File.separator)));
            MockMultipartFile file = new MockMultipartFile("file", "honey-cake.jpg", "multipart/form-data", byteArray);
            //Save the image into database
            itemImageService.saveItemImage(id,file);
            //Create another mock image file and save it into database
            file = new MockMultipartFile("file", "honey-cake2.jpg", "multipart/form-data", byteArray);
            itemImageService.saveItemImage(id,file);

            //Test that the image list can be correctly retrieved by checking the list size
            assertEquals(2,itemImageService.getAllItemImages().size());
            //Check the ID of each item in the list to match the expected ID value
            assertEquals(id,itemImageService.getAllItemImages().get(0).getItem().getId());
            assertEquals(id,itemImageService.getAllItemImages().get(1).getItem().getId());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteItemImageTests(){
        //Add new item before conducting test case
        Item newItem = new Item("Hot dog","Very hot","dog.jpg",new BigDecimal("3.99"),"Hotdog",true);
        Long id = itemService.saveItem(newItem);
        try{
            //Create a mock image file from a byte array
            byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\", File.separator)));
            MockMultipartFile file = new MockMultipartFile("file", "honey-cake.jpg", "multipart/form-data", byteArray);
            //Save the image into database
            itemImageService.saveItemImage(id,file);
            //Create another mock image file and save it into database
            file = new MockMultipartFile("file", "honey-cake2.jpg", "multipart/form-data", byteArray);
            itemImageService.saveItemImage(id,file);

            //Check that both images has been saved by checking the image list size
            assertEquals(2,itemImageService.getAllItemImages().size());

            //Delete one image from the database, then check the list size
            itemImageService.deleteItemImage(itemImageService.getAllItemImages().get(0));
            assertEquals(1,itemImageService.getAllItemImages().size());

            //Delete another image from the database, then check the list size
            itemImageService.deleteItemImage(itemImageService.getAllItemImages().get(0));
            assertEquals(0,itemImageService.getAllItemImages().size());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}