package com.example.ordersystem.controller;

import com.example.ordersystem.model.*;
import com.example.ordersystem.service.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    /**
     * Mapping for the path to the shop page which displays all items in the database
     * @param model - The ModelMap which contains the shop items information to send to the template via Thymeleaf
     * @param page - The current page number
     * @param sortField - The sort field to sort items by name or price. The default is sort by ID.
     * @param filterField - The filter field to filter items by category. The default is to show all.
     * @param searchField - The search field to search items. The default is an empty string.
     * @return A String which is the processed shop template, populated with the required items
     */
    @GetMapping("/shop")
    public String listAll(ModelMap model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(name="sortField", defaultValue = "id") String sortField,
                          @RequestParam(name="filterField", defaultValue = "All") String filterField ,
                          @RequestParam(name="search-input", defaultValue = "") String searchField
    ) {


        // The total number of items
        int totalNum = itemService.getAllItems().size();

        //Create pagination for the items
        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max number of items in a page
        int pageSize = pagination.getPageSize();

        // Check if value of sortField is valid
        String[] validSort = new String[]{"id","name","priceHTL","priceLTH"};
        if(!Arrays.asList(validSort).contains(sortField)){
            sortField = "id";
        }

        // Get all items
        List<Item> fullItemList = itemService.getAllItems();
        //Create iterator to iterate through item list
        Iterator<Item> itemIterator = fullItemList.listIterator();

        // Get a list of categories of items
        List<String> categoryListwithDuplicates = new ArrayList<>();
        categoryListwithDuplicates.add("All");
        //Use iterator to iterate through the item list and add all categories
        while(itemIterator.hasNext()){
            categoryListwithDuplicates.add(itemIterator.next().getCategory());
        }
        List<String> categoryList = new ArrayList<String>(new LinkedHashSet<>(categoryListwithDuplicates));

        // Check if value of filterField is valid
        if(!categoryList.contains(filterField)){
            filterField = "All";
        }

        // Separate the final item list into different pages
        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize, filterField, sortField, searchField);

        // Get pagination for item list when Filter is used
        pagination = new Pagination(itemService.findNumOfSearchedItems(filterField, searchField)+1, page);

        //Add the fields and item list to the model so Thymeleaf can process it on the frontend template
        model.addAttribute("filterField", filterField);
        model.addAttribute("searchField", searchField);
        model.addAttribute("sortField", sortField);
        model.addAttribute("shopList", shopList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pagination", pagination);

        unifiedService.getCartInfo(model);

        return "shop";
    }

    /**
     * Mapping to view the details page of a specific item in the shop
     * @param model - The ModelMap which contains the specific item information to send to the template via Thymeleaf
     * @param id - The ID of the item to get
     * @return A String which is the processed frontend template
     */
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

    /**
     * Mapping to the item form that Admins can use to add and edit items
     * @param model - The ModelMap which contains the information to send to the template via Thymeleaf
     * @return A String which is the processed form template
     */
    @GetMapping(path = "/item-form")
    public String itemForm(ModelMap model){
        unifiedService.getCartInfo(model);
        return "item-form";
    }

    /**
     * Mapping for the item list that Admins can use to perform RUD operations on all items in the database
     * @param model - The ModelMap which contains all items information to send to the template via Thymeleaf
     * @return A String which is the processed template of the item list
     */
    @GetMapping(path = "/item-list")
    public String itemList(ModelMap model){
        List<Item> itemList = itemService.getAllItemsSortedId();
        model.addAttribute("itemList",itemList);
        unifiedService.getCartInfo(model);
        return "item-list";
    }

    /**
     * Mapping to the endpoint which lets Admins change the availability status of a specific item by ID
     * @param id - The ID of the item to change status
     * @return A String which is the updated frontend template
     */
    @GetMapping(path = "/items/setavailability/{id}")
    public String changeAvailability(@PathVariable Long id){
        itemService.changeAvailability(id);
        return "redirect:/item-list";
    }

    /**
     * Mapping for endpoint to display an item image from a byte array by ID
     * @param imageId - The ID of the image to display
     * @param response - The HTTP response to which the image will be sent
     * @throws IOException
     */
    @GetMapping(value = "/itemimage/{image_id}")
    public void getImage(@PathVariable("image_id") Long imageId, HttpServletResponse response) throws IOException {
        InputStream is = new ByteArrayInputStream(itemImageService.getItemImageById(imageId).getImage());
        IOUtils.copy(is, response.getOutputStream());
    }
}
