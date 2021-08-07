/*package com.example.ordersystem;

import com.example.ordersystem.model.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderSystemApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() throws IOException {

        try {
            URL url = new URL(TestConfig.URL + "students");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            Gson gson = new Gson();
            String json = stringBuilder.toString();
            List<Student> students = gson.fromJson(json, new TypeToken<List<Student>>() {
            }.getType());
            //String s = stringBuilder.toString();
            assertEquals(students.get(0).getName(), "Thanh");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}*/
