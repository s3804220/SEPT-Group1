package com.example.ordersystem.controller;

import com.example.ordersystem.model.Item;
import com.example.ordersystem.model.Pagination;
import com.example.ordersystem.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/shop")
    public String listAll(Model model, @RequestParam(defaultValue = "1") int page) {

        // The number of total items
        int totalNum = itemService.findTotal();

        Pagination pagination = new Pagination(totalNum, page);

        int beginIndex = pagination.getBeginIndex();

        // Max num of items in a page
        int pageSize = pagination.getPageSize();

        List<Item> shopList = itemService.findListPaging(beginIndex, pageSize);

        model.addAttribute("shopList", shopList);
        model.addAttribute("pagination", pagination);

        return "shop";
    }

    @GetMapping("/shop-details")
    public String readDetail(Model model, @RequestParam("id") Long id) throws Exception {

        model.addAttribute("shopDetail", itemService.getItem(id).get());

        return "shop-details";
    }

    @GetMapping(path = "/item-form")
    public String itemForm(){
        return "item-form";
    }

    @GetMapping(path = "/item-list")
    public String itemList(){
        return "item-list";
    }
}
