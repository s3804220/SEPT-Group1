package com.example.ordersystem.config;

import com.example.ordersystem.model.Item;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Properties;

/**
 * Created by CoT on 10/14/17.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Configuration
@EnableTransactionManagement
//@EnableWebMvc
@EnableJpaRepositories("com.example.ordersystem.repository")
@ComponentScan(basePackages = {"com.example.ordersystem"})
@EntityScan("com.example.ordersystem.model")
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public Item item() {
        return new Item();
    }

    /*@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/classes/static/");
    }*/

    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory(){

        Properties properties = new Properties();
        //For Postgresql
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        //For mysql
        //properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "update");

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

        sessionFactoryBean.setPackagesToScan("com.example.ordersystem.model");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        // Heroku's database credentials
//        dataSource.setUrl("jdbc:postgresql://ec2-44-197-40-76.compute-1.amazonaws.com:5432/dfh63mee24494k");
//        dataSource.setUsername("renfdjhmcquulb");
//        dataSource.setPassword("4e9deeb856b3dd2fd6ce9d26ff442a36298163355cb85354a4f9f499a68520b4");

        // Local database credentials
        dataSource.setUrl("jdbc:postgresql://localhost:5432/cosc2101");
        dataSource.setUsername("postgres");
        dataSource.setPassword("super123");
      
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
//        dataSource.setUsername("pg");
//        dataSource.setPassword("1234");


        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setHibernateProperties(properties);

        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager tx = new HibernateTransactionManager(sessionFactory);
        return tx;
    }

}


