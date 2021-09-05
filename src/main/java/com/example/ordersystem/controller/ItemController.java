package com.example.ordersystem.controller;

import com.example.ordersystem.model.*;
import com.example.ordersystem.service.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemImageService itemImageService;
    @Autowired
    private UnifiedService unifiedService;


    @GetMapping("/shop")
    public String listAll(ModelMap model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(name="sortField", defaultValue = "id") String sortField,
                          @RequestParam(name="filterField", defaultValue = "All") String filterField ,
                          @RequestParam(name="search-input", defaultValue = "") String searchField
    ) {


        // The number of total items
        int totalNum = itemService.getAllItems().size();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        // Check if value of sortFild is valid
        String[] validSort = new String[]{"id","name","priceHTL","priceLTH"};
        if(!Arrays.asList(validSort).contains(sortField)){
            sortField = "id";
        }

        // Get all items
        List<Item> fullItemList = itemService.getAllItems();


        // Get a list of categories of items
        List<String> categoryListwithDuplicates = new ArrayList<>();
        categoryListwithDuplicates.add("All");
        for (Item item : fullItemList) {
            categoryListwithDuplicates.add(item.getCategory());
        }
        List<String> categoryList = new ArrayList<String>(new LinkedHashSet<>(categoryListwithDuplicates));


        // Check if value of filterFild is valid
        if(!categoryList.contains(filterField)){
            filterField = "All";
        }

        // Pagination
        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize, filterField, sortField, searchField);


        // Get pagination when Filter is used
        pagination = new Pagination(itemService.findNumOfSearchedItems(filterField, searchField)+1, page);
//        System.out.println("@@@@@@ itemService.findNumOfFilteredItems(filterField): "
//                +itemService.findNumOfSearchedItems(filterField, searchField));
//        System.out.println("@@@@@@ pagination.getTotalPages: "+pagination.getTotalPages());
//        System.out.println("@@@@@@ searchField : " +searchField);
        System.out.println("@@@@@CategoryList: "+categoryList);

        model.addAttribute("filterField", filterField);
        model.addAttribute("searchField", searchField);
        model.addAttribute("sortField", sortField);
        model.addAttribute("shopList", shopList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pagination", pagination);

        unifiedService.getCartInfo(model);

        return "shop";
    }

    @GetMapping("/shop-details")
    public String readDetail(ModelMap model, @RequestParam("id") Long id) {

        if(itemService.getItem(id).isPresent()){
            model.addAttribute("shopDetail", itemService.getItem(id).get());
        }else{
            model.addAttribute("shopDetail",null);
        }

        unifiedService.getCartInfo(model);

        return "shop-details";
    }

    @GetMapping(path = "/item-form")
    public String itemForm(ModelMap model){
        unifiedService.getCartInfo(model);
        return "item-form";
    }

    @GetMapping(path = "/item-list")
    public String itemList(ModelMap model){
        List<Item> itemList = itemService.getAllItemsSortedId();
        model.addAttribute("itemList",itemList);
        unifiedService.getCartInfo(model);
        return "item-list";
    }

    @GetMapping(path = "/items/setavailability/{id}")
    public String changeAvailability(@PathVariable Long id){
        itemService.changeAvailability(id);
        return "redirect:/item-list";
    }


    @GetMapping(value = "/itemimage/{image_id}")
    public void getImage(@PathVariable("image_id") Long imageId, HttpServletResponse response) throws IOException {
        InputStream is = new ByteArrayInputStream(itemImageService.getItemImageById(imageId).getImage());
        IOUtils.copy(is, response.getOutputStream());
    }
}
