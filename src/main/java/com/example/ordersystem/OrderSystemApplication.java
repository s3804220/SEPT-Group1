package com.example.ordersystem;

import com.example.ordersystem.model.Cart;
import com.example.ordersystem.model.Shop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSystemApplication.class, args);


//                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.register(AppConfig.class);
//
//        context.refresh();
//
//        StudentService studentService = context.getBean(StudentService.class);
//
//        System.out.println(studentService.findStudents("Student"));



            /*URL url = new URL("http://localhost:8080/students");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";

            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("line"+line);
                stringBuilder.append(line);
            }

            String s = stringBuilder.toString();

            System.out.println(s);*/
    }

}
