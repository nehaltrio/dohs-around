package com.ecom.dohsaround.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.dohsaround.dto.CategoryDto;
import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.CategoryRepository;
import com.ecom.dohsaround.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> findAllByShop(Shop shop) {

        return repo.findAllByAdmin(shop);
    }

    @Override
    public List<Category> findAllByShopActivated(Shop shop) {
        return repo.findAllByAdminActivated(shop);
    }

    @Override
    public Category save(Category category, Shop shop) {
        Category categorySave = new Category(category.getName(), shop);

        return repo.save(categorySave);
    }

    @Override
    public Category findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate= repo.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_activated(category.is_activated());
            categoryUpdate.set_deleted(category.is_deleted());
        }catch (Exception e){
            e.printStackTrace();
        }
        return repo.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Category category = repo.getById(id);
        category.set_deleted(true);
        category.set_activated(false);
        repo.save(category);
    }

    @Override
    public void enabledById(Long id) {
        Category category = repo.getById(id);
        category.set_activated(true);
        category.set_deleted(false);
        repo.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {
        return repo.findAllByActivated();
    }

    @Override
    public List<CategoryDto> getCategoryAndProduct(String shopName) {
        return repo.getCategoryAndProduct(shopName);
    }


}
