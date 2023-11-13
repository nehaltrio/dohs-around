package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.model.CartItem;
import com.ecom.dohsaround.model.Shop;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select c from CartItem c where c.product.shop=?1")
    List<CartItem> findCartItemsByShop(Shop shop);
}
