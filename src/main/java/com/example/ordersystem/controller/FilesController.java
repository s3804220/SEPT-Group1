package com.example.ordersystem.controller;

import com.example.ordersystem.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("http://localhost:8080")
public class FilesController {
    @Autowired
    FilesStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam HashMap<String, MultipartFile> files) {
        String message = "";
        if(files != null){
            //Get the item's ID and image files from the request
            // and run a loop to save each image into the folder with that item's ID
            for(String key:files.keySet()){
                String[] parts = key.split(Pattern.quote("|"));
                MultipartFile file = files.get(key);
                try {
                    storageService.save(file, parts[1]);
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

    @DeleteMapping("/deletefiles/{id}")
    //Delete all image files in the folder corresponding with the item's ID
    public void deleteFiles(@PathVariable Long id){
        storageService.deleteAll(id.toString());
    }
}
