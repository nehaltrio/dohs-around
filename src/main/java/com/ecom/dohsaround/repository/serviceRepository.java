package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecom.dohsaround.model.Service;
import com.ecom.dohsaround.model.Shop;

import java.util.List;

public interface serviceRepository extends JpaRepository<Service, Long> {

    @Query("select s from Service s where s.shop=?1")
    List<Service> findServiceByShop(Shop shop);

    @Query("select s from Service s where (s.description like %?1% or s.serviceName like %?1%) and s.shop=?2")
    List<Service> searchServiceByShop(String keyword, Shop shop);

    @Query("select count(s.id) from Service s where s.shop.id=?1")
    int countServiceByAdmin_Id(Long id);
}
