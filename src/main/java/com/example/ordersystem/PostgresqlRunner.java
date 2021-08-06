// May be used to make table in a db of postgres

//package com.example.ordersystem;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.*;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Statement;
//
//@Component
//public class PostgresqlRunner implements ApplicationRunner {
//
//
//    private DataSource dataSource;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public PostgresqlRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
//        this.dataSource = dataSource;
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        try (Connection connection = dataSource.getConnection()){
//            System.out.println(dataSource.getClass());
//            System.out.println(connection.getMetaData().getURL());
//            System.out.println(connection.getMetaData().getUserName());
//
//            Statement statement = connection.createStatement();
//            String sql = "CREATE TABLE t_product(product_no INTEGER NOT NULL, product_name VARCHAR(255), PRIMARY KEY (product_no))";
//            statement.executeUpdate(sql);
//        }
//        jdbcTemplate.execute("INSERT INTO t_product VALUES (1, 'Big shirt')");
//    }
//}