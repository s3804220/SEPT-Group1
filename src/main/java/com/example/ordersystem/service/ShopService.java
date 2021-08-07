package com.example.ordersystem.service;

import com.example.ordersystem.model.Shop;
import com.example.ordersystem.model.Student;
import com.example.ordersystem.repository.CustomShopRepository;
import com.example.ordersystem.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ordersystem.repository.StudentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class ShopService implements CustomShopRepository {



    @PersistenceContext
    private EntityManager em;

    private ShopRepository shopRepository;


    @Autowired
    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }


    public void saveShop(Shop shop){
        shopRepository.save(shop);
    }


    @Override
    public Optional<Shop> findById(Long id) {
        return shopRepository.findById(id);
    }

    @Override
    public Shop findShopById(Long id) {
        return shopRepository.findById(id).orElse(new Shop());
    }


    public List<Shop> getAllShops(){
        return shopRepository.findAll();
    }


    public int findTotal() {
        return ((Number) em.createQuery("select count(*) from Shop")
                .getSingleResult()).intValue();
    }

    public List<Shop> findListPaging(int startIndex, int pageSize) {
        return em.createQuery("select b from Shop b", Shop.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Shop getShop(Long id){
        return shopRepository.getById(id);
    }

    public void deleteShop(Long id){
        shopRepository.delete(getShop(id));
    }
}
