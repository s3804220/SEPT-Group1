package com.example.ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Transactional
@Service
public class FilesStorageService {

    //Create new folder (if not exists) with name associated with ID to store each item's images
    public Path init(String id) {
        Path itemRoot = Paths.get("target\\classes\\static\\img\\upload\\item".replace("\\", File.separator)+id);
        try {
            Files.createDirectories(itemRoot);
            return itemRoot;
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    //Save image file into the folder corresponding with that item's ID
    public void save(MultipartFile file, String id) {
        try {
            Files.copy(file.getInputStream(), init(id).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    //Save image file into the shop folder
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get("target\\classes\\static\\img\\shop".replace("\\", File.separator)).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    //Delete the whole folder corresponding with that item's ID
    public void deleteAll(String id) {
        FileSystemUtils.deleteRecursively(init(id).toFile());
    }
}
