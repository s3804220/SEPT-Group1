package com.example.ordersystem.controller;

import com.example.ordersystem.model.ItemImage;
import com.example.ordersystem.service.ItemImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class ItemImageController {
    @Autowired
    private ItemImageService itemImageService;

    @PostMapping("/item/{id}/image")
    public ResponseEntity<String> handleImagePost(@PathVariable String id, @RequestParam HashMap<String, MultipartFile> files){
        String message = "";
        if(files != null){
            //Get the item's ID and image files from the request
            // and run a loop to save each image into the folder with that item's ID
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
            //Return the status as a response to the website
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File array is NULL!");
    }

    @DeleteMapping("/item/{id}/image")
    public void deleteItemImage(@PathVariable String id){
        List<ItemImage> itemImageList = itemImageService.getAllItemImages();
        for (ItemImage itemImage: itemImageList){
            if(itemImage.getItem().getId()==Long.valueOf(id)){
                itemImageService.deleteItemImage(itemImage);
            }
        }
    }
}
