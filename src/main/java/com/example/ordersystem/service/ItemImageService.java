package com.example.ordersystem.service;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.ItemImage;
import com.example.ordersystem.repository.ItemImageRepository;
import com.example.ordersystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * This class is the service layer for performing CRUD operations on item images
 */
@Transactional
@Service
public class ItemImageService {
    @Autowired
    private ItemImageRepository itemImageRepository;
    @Autowired
    private ItemRepository itemRepository;

    //Function to save the item image as a new record in the table
    public void saveItemImage(Long itemId, MultipartFile file) {

        try {
            Item item = itemRepository.findById(itemId).get();
            ItemImage newImage = new ItemImage();

            //Convert the multipart media file into a byte array to be stored in the database
            byte[] byteObjects = new byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            //Link the image to the item, then save its content to the database
            newImage.setItem(item);
            newImage.setImage(byteObjects);
            itemImageRepository.save(newImage);

        } catch (IOException e) {
            System.out.println("An error occur when saving the image!");
            e.printStackTrace();
        }
    }

    //Function to get an item image by ID
    public ItemImage getItemImageById(Long id){
        return itemImageRepository.findById(id).get();
    }

    //Function to get all item images available in the database
    public List<ItemImage> getAllItemImages(){
        return itemImageRepository.findAll();
    }

    //Function to delete a specific item image
    public void deleteItemImage(ItemImage itemImage){
        itemImageRepository.delete(itemImage);
    }
}
