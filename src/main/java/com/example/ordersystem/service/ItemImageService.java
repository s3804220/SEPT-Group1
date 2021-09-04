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

@Transactional
@Service
public class ItemImageService {
    @Autowired
    private ItemImageRepository itemImageRepository;
    @Autowired
    private ItemRepository itemRepository;

    public void saveItemImage(Long itemId, MultipartFile file) {

        try {
            Item item = itemRepository.findById(itemId).get();
            ItemImage newImage = new ItemImage();

            byte[] byteObjects = new byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            newImage.setItem(item);
            newImage.setImage(byteObjects);
            itemImageRepository.save(newImage);

        } catch (IOException e) {
            System.out.println("An error occur when saving the image!");
            e.printStackTrace();
        }
    }

    public ItemImage getItemImageById(Long id){
        return itemImageRepository.findById(id).get();
    }

    public List<ItemImage> getAllItemImages(){
        return itemImageRepository.findAll();
    }

    public void deleteItemImage(ItemImage itemImage){
        itemImageRepository.delete(itemImage);
    }
}
