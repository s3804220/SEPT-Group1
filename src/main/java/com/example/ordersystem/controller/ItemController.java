package com.example.ordersystem.controller;

import com.example.ordersystem.model.*;
import com.example.ordersystem.service.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class is used for routing shop and item-related pages and their handlers.
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemImageService itemImageService;
    @Autowired
    private UnifiedService unifiedService;

    //Map the path for the shop page to display all items in the database
    @GetMapping("/shop")
    public String listAll(ModelMap model, @RequestParam(defaultValue = "1") int page) {

        // The total number of items
        int totalNum = itemService.findTotal();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max number of items in a page
        int pageSize = pagination.getPageSize();

        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize);

        model.addAttribute("shopList", shopList);
        model.addAttribute("pagination", pagination);

        unifiedService.getCartInfo(model);

        return "shop";
    }

    //Map the path for the details page of a specific item in the shop
    @GetMapping("/shop-details")
    public String readDetail(ModelMap model, @RequestParam("id") Long id) {

        //If the id returns a valid item, send its details to the page. If not, return a null value
        if(itemService.getItem(id).isPresent()){
            model.addAttribute("shopDetail", itemService.getItem(id).get());
        }else{
            model.addAttribute("shopDetail",null);
        }

        unifiedService.getCartInfo(model);

        return "shop-details";
    }

    //Map the path for the item form that Admins can use to add and edit items
    @GetMapping(path = "/item-form")
    public String itemForm(ModelMap model){
        unifiedService.getCartInfo(model);
        return "item-form";
    }

    //Map the path for the item list that Admins can use to view all items in the database
    @GetMapping(path = "/item-list")
    public String itemList(ModelMap model){
        List<Item> itemList = itemService.getAllItemsSortedId();
        model.addAttribute("itemList",itemList);
        unifiedService.getCartInfo(model);
        return "item-list";
    }

    //Map the endpoint to let Admins change the availability status of a specific item by ID
    @GetMapping(path = "/items/setavailability/{id}")
    public String changeAvailability(@PathVariable Long id){
        itemService.changeAvailability(id);
        return "redirect:/item-list";
    }

    //Map the path to display an item image from a byte array by ID
    @GetMapping(value = "/itemimage/{image_id}")
    public void getImage(@PathVariable("image_id") Long imageId, HttpServletResponse response) throws IOException {
        InputStream is = new ByteArrayInputStream(itemImageService.getItemImageById(imageId).getImage());
        IOUtils.copy(is, response.getOutputStream());
    }
}
