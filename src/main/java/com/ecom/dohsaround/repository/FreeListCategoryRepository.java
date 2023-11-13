package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecom.dohsaround.dto.FreeListCategoryDto;
import com.ecom.dohsaround.model.FreeListCategory;

import java.util.List;

public interface FreeListCategoryRepository extends JpaRepository<FreeListCategory, Long> {

    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false")
    List<FreeListCategory> findAllByActivated();

    @Query("select new com.ecom.dohsaround.dto.FreeListCategoryDto(c.id, c.name, count(p.category.id)) from FreeListCategory c inner join FreeListProduct p on p.category.id = c.id " +
            " where c.is_activated = true and c.is_deleted = false and p.is_activated=true  group by c.id")
    List<FreeListCategoryDto> getCategoryAndProduct();
}
