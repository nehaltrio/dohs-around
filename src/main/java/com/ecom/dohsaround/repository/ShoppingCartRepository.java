package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.ShoppingCart;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {


    @Modifying
    @Query("delete from ShoppingCart where customer=?1")
    void deleteShoppingCartByCustomer(Customer customer);


}
