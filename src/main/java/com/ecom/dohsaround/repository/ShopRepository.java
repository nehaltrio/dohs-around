package com.ecom.dohsaround.repository;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;

import java.util.HashSet;
import java.util.List;




@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("select s from Shop s where s.username=?1")
    Shop findByUsername(String username);


    @Query("select a from Shop a where a.shopName=?1")
    Shop getAdminByShopName(String shopName);

    @Modifying
    @Query("update Shop a set a.themeColor=?2 where a.id=?1")
    void setShopColorByShop(Long id, String color);


    @Query("select s from Shop s order by s.shopName desc")
    List<Shop> findTopFiveShop(PageRequest of);

    @Query("select s from Shop s where s.shopName=?1")
    Shop findAdminByShopName(String shopName);

    @Query("from Shop where active=true")
    List<Shop> findAllShops();

    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE %?1% OR (SELECT c.name FROM Category c WHERE p.category.id = c.id) LIKE %?1%")
    List<Product> searchByProduct(String keyword);

    @Query(value = "SELECT p.shop FROM Product p WHERE p.name LIKE %?1% OR (SELECT c.name FROM Category c WHERE p.category.id = c.id) LIKE %?1%")
    HashSet<Shop> searchByShop(String keyword);
}
