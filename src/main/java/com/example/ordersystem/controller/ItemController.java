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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemImageService itemImageService;
    @Autowired
    private UnifiedService unifiedService;

    @GetMapping("/shop")
    public String listAll(ModelMap model, @RequestParam(defaultValue = "1") int page) {

        // The number of total items
        int totalNum = itemService.findTotal();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize);

        model.addAttribute("shopList", shopList);
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
