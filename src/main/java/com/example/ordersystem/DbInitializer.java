package com.example.ordersystem;

import com.example.ordersystem.controller.*;
import com.example.ordersystem.model.*;
import com.example.ordersystem.repository.*;
import com.example.ordersystem.security.*;
import com.example.ordersystem.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.init-db", havingValue="true")
@AllArgsConstructor
public class DbInitializer implements CommandLineRunner {
    @Autowired
    private AccountService accountService;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private ItemService itemService;

    @Override
    public void run(String... args) throws Exception {
        List<Account> accountList = accountService.getAllAccounts();

//        for (Account temp : accountList){
//            accountService.deleteAccount(temp.getId());
//        }
//        this.accountRepository.deleteAll();

//        //Initialize admin account
//        Account admin = new Account("John", "Doe", "123 Tech Street", "0204648395", "admin@gmail.com", "admin", AccountRole.ADMIN);
//        accountService.signUpAccount(admin);
//        accountService.setAccountRole(admin.getId(), AccountRole.ADMIN);
//
//        //Initialize user account
//        Account user1 = new Account("Jeffrey", "Babble", "456 Flower Lane", "0903682439", "user@gmail.com", "password", AccountRole.USER);
//        accountService.signUpAccount(user1);

//        Account user1 = accountService.getAccountById((long) 2); // id=2 : Jeffrey

        Item item1 = new Item("Cream cupcake", "A delicious cupcake with vanilla cream to brighten your day", "product-1.jpg", new BigDecimal("21.00"), "Cupcake",true);
        Item item2 = new Item("Chocolate cupcake","A delicious cupcake with chocolate toppings to sweeten your day", "product-2.jpg", new BigDecimal("22.00"),"Cupcake",true);
        Item item3 = new Item("Unicorn Cake","A colorfully decorated cake. Brings some magical vanilla and cream to your life","cake-1.jpg",new BigDecimal("40.00"),"Cake",true);
        Item item4 = new Item("Chocolate & Raspberry Cake","The ultimate combo. A creamy cake covered with sweet chocolate and fresh raspberries","cake-2.jpg|cake-2-2.jpg",new BigDecimal("35.00"),"Cake",true);
        Item item5 = new Item("Lemon Flower Sandwich Biscuits","Two biscuits sandwiched together. The buttery flavor and zingy taste of lemon await you","biscuit-1.jpg",new BigDecimal("10.00"),"Biscuit",true);
        Item item6 = new Item("Fall Leaves Sugar Biscuits","Colorful biscuits with a sprinkle of sugar to sweeten the season","biscuit-2.jpg",new BigDecimal("15.50"),"Biscuit",true);
        Item item7 = new Item("Pink Strawberry Donut","A fluffy and pink donut with colorful sprinkles. Yummy","donut-1.jpeg",new BigDecimal("15.00"),"Donut",true);
        Item item8 = new Item("Smiley Face Donut","Look how happy that donut is. Doesn't it put a smile on your face?","donut-2.jpg|donut-2-2.jpg|donut-2-3.jpg|donut-2-4.png",new BigDecimal("17.00"),"Donut",true);

        itemService.saveItem(item1);
        itemService.saveItem(item2);
        itemService.saveItem(item3);
        itemService.saveItem(item4);
        itemService.saveItem(item5);
        itemService.saveItem(item6);
        itemService.saveItem(item7);
        itemService.saveItem(item8);

        List<Item> itemList = itemService.getAllItems();

        for(Item item: itemList){
            String[] strings = item.getItemImage().split("[|]");
            for(String imgname: strings){
                byte[] byteArray = Files.readAllBytes(Paths.get("src\\main\\resources\\static\\img\\shop\\"+imgname.replace("\\", File.separator)));
                MockMultipartFile file = new MockMultipartFile("file", imgname, "multipart/form-data", byteArray);
                filesStorageService.save(file, item.getId().toString());
            }
        }
    }

}
