package com.example.ordersystem.controller;

import com.example.ordersystem.model.ItemImage;
import com.example.ordersystem.service.ItemImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * This class is used for routing RESTful endpoints to perform CRUD on item images.
 */
@RestController
public class ItemImageController {
    @Autowired
    private ItemImageService itemImageService;

    /**
     * Mapping of the endpoint to let Admins upload new images of a specific item by ID
     * @param id - The ID of the item to upload image
     * @param files - The HashMap which contains String as a key, and multipart files which represent the image(s) to upload
     * @return A response to the client, whether the upload was successful or not
     */
    @PostMapping("/item/{id}/image")
    public ResponseEntity<String> handleImagePost(@PathVariable String id, @RequestParam HashMap<String, MultipartFile> files){
        String message = "";
        if(files != null){
            //Get the item's ID and image files from the request
            // and run a loop to save each image into the database with the item's ID
            for(String key:files.keySet()){
                MultipartFile file = files.get(key);
                try {
                    itemImageService.saveItemImage(Long.valueOf(id),file);
                    message += "Uploaded the file successfully: " + file.getOriginalFilename() + "!\n";
                } catch (Exception e) {
                    message = "Could not upload the file: " + file.getOriginalFilename() + "!\n"+ Arrays.toString(e.getStackTrace());
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
                }
            }
            //Return the status as a response to the client website
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File array is NULL!");
    }

    /**
     * Mapping of the endpoint to let Admins delete all images of a specific item from the database
     * @param id - The ID of the item whose images will be deleted
     */
    @DeleteMapping("/item/{id}/image")
    public void deleteItemImage(@PathVariable String id){
        //Get a list of all item images
        List<ItemImage> itemImageList = itemImageService.getAllItemImages();
        //Create iterator to iterate through the image list
        Iterator<ItemImage> imageIterator = itemImageList.listIterator();
        while (imageIterator.hasNext()){
            //Get the next item image
            ItemImage nextImage = imageIterator.next();
            //Delete it if its item ID matches the ID of the item to delete
            if(Objects.equals(nextImage.getItem().getId(), Long.valueOf(id))){
                itemImageService.deleteItemImage(nextImage);
            }
        }
    }
}
