package com.example.ordersystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilesStorageServiceTest {
    @Autowired
    private FilesStorageService filesStorageService;

    @AfterEach
    public void clearFolder(){
        FileSystemUtils.deleteRecursively(Paths.get("target\\classes\\static\\img\\upload".replace("\\",File.separator)).toFile());
    }

    @Test
    public void createDirectoryTests() {
        //Test directory creation with a normal ID and a really large ID
        assertTrue(Files.isDirectory(filesStorageService.init("10")));
        assertTrue(Files.isDirectory(filesStorageService.init("999")));
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    //Test naming restrictions for Windows only
    public void createDirectoryWindowsTests(){
        //Test directory creation with invalid characters
        assertThrows(InvalidPathException.class, () -> Files.isDirectory(filesStorageService.init("???;")));

        //Test directory creation with both valid and invalid characters
        assertThrows(InvalidPathException.class, () -> Files.isDirectory(filesStorageService.init("test///*")));
    }

    @Test
    public void MultipartFileSaveTests() throws IOException {
        byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\",File.separator)));
        //Mock new multipart file with valid file name
        MockMultipartFile file = new MockMultipartFile("file", "Honeycomb-cake.jpg", "multipart/form-data", byteArray);
        filesStorageService.save(file, "10");
        //Test that file can be saved successfully into a valid directory
        assertTrue(new File("target\\classes\\static\\img\\upload\\item10\\Honeycomb-cake.jpg".replace("\\",File.separator)).isFile());
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    //Test naming restrictions for Windows only
    public void MultipartFileSaveWindowsTests() throws IOException {
        byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\",File.separator)));
        //Mock new multipart file with valid file name
        MockMultipartFile file = new MockMultipartFile("file", "Honeycomb-cake.jpg", "multipart/form-data", byteArray);

        //Test that file cannot be saved into an invalid directory
        assertThrows(RuntimeException.class, () -> filesStorageService.save(file, "???"));
        assertFalse(new File("target\\classes\\static\\img\\upload\\item???\\Honeycomb-cake.jpg".replace("\\",File.separator)).isFile());

        //Mock new multipart file with invalid file name
        MockMultipartFile file2 = new MockMultipartFile("file", "???.jpg", "multipart/form-data", byteArray);
        //Test that invalid file cannot be saved into a directory
        assertThrows(RuntimeException.class, () -> filesStorageService.save(file2, "10"));
        assertFalse(new File("target\\classes\\static\\img\\upload\\item10\\???.jpg".replace("\\",File.separator)).isFile());
    }

    @Test
    public void deleteAll() throws IOException {
        //Mock new multipart file for testing
        byte[] byteArray = Files.readAllBytes(Paths.get("src\\test\\resources\\Honeycomb.jpg".replace("\\",File.separator)));
        MockMultipartFile file = new MockMultipartFile("file", "Honeycomb-cake.jpg", "multipart/form-data", byteArray);
        filesStorageService.save(file, "10");

        //Delete the directory containing the file above
        filesStorageService.deleteAll("10");
        //Test that the directory and its file have been deleted
        assertFalse(new File("target\\classes\\static\\img\\upload\\item10\\Honeycomb-cake.jpg".replace("\\",File.separator)).isFile());
        assertFalse(Files.isDirectory(Paths.get("target\\classes\\static\\img\\upload\\item10".replace("\\",File.separator))));

        //Mock another multipart file for testing
        byte[] byteArray2 = Files.readAllBytes(Paths.get("src\\test\\resources\\Vanilla Cake.jpg".replace("\\",File.separator)));
        MockMultipartFile file2 = new MockMultipartFile("file", "Vanilla-cake.jpg", "multipart/form-data", byteArray2);
        //Save 2 files into a directory
        filesStorageService.save(file, "999");
        filesStorageService.save(file2, "999");

        //Delete the directory containing multiple files
        filesStorageService.deleteAll("999");
        //Test that the directory and all its files have been deleted
        assertFalse(new File("target\\classes\\static\\img\\upload\\item999\\Honeycomb-cake.jpg".replace("\\",File.separator)).isFile());
        assertFalse(new File("target\\classes\\static\\img\\upload\\item999\\Vanilla-cake.jpg".replace("\\",File.separator)).isFile());
        assertFalse(Files.isDirectory(Paths.get("target\\classes\\static\\img\\upload\\item999".replace("\\",File.separator))));
    }
}