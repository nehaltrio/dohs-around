package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.dto.CategoryDto;
import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.Shop;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false")
    List<Category> findAllByActivated();


    /*Customer*/
    @Query("select new com.ecommerce.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) from Category c inner join Product p on p.category.id = c.id " +
            " where c.is_activated = true and c.is_deleted = false and p.is_activated=true and c.shop.shopName=?1 group by c.id")
    List<CategoryDto> getCategoryAndProduct(String shopName);


    @Query("select c from Category c where c.shop=?1")
    List<Category> findAllByAdmin(Shop shop);

    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false and c.shop=?1")
    List<Category> findAllByAdminActivated(Shop shop);

    @Query("select count(c.id) from Category c where c.shop.id=?1")
    int countCategoryByAdmin_Id(Long id);
}
