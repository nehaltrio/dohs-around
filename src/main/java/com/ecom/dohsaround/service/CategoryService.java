package com.ecom.dohsaround.service;



import java.util.List;

import com.ecom.dohsaround.dto.CategoryDto;
import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.Shop;

public interface CategoryService {
    /*Shop*/
    List<Category> findAllByShop(Shop shop);
    List<Category> findAllByShopActivated(Shop shop);
    Category save(Category category, Shop shop);
    Category findById(Long id);
    Category update(Category category);
    void deleteById(Long id);
    void enabledById(Long id);
    List<Category> findAllByActivated();

    /*Customer*/
    List<CategoryDto> getCategoryAndProduct(String shopName);




}
