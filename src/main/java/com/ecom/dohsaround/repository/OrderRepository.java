package com.ecom.dohsaround.repository;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecom.dohsaround.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("select o from Order o where o.product.shop.id=?1")
    List<Order> findOrderByShopId(Long id);

    @Query("select count(o.id) from Order o where o.product.shop.id=?1")
    int countOrderByAdmin_Id(Long id);

    @Query(value = "select o from Order o where o.product.shop.id=?1 order by o.orderDate desc")
    List<Order> findOrderByShopIdRecent(Long id, PageRequest pageRequest);
}
