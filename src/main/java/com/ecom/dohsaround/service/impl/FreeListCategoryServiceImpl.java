package com.ecom.dohsaround.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.dohsaround.dto.FreeListCategoryDto;
import com.ecom.dohsaround.model.FreeListCategory;
import com.ecom.dohsaround.repository.FreeListCategoryRepository;
import com.ecom.dohsaround.service.FreeListCategoryService;

import java.util.List;

@Service
public class FreeListCategoryServiceImpl implements FreeListCategoryService {

    @Autowired
    private FreeListCategoryRepository repo;

    @Override
    public List<FreeListCategory> findAll() {
        return repo.findAll();
    }

    @Override
    public List<FreeListCategory> findAllByActivated() {
        return repo.findAllByActivated();
    }

    @Override
    public FreeListCategory save(FreeListCategory category) {
        FreeListCategory categorySave = new FreeListCategory(category.getName());
        return repo.save(categorySave);
    }

    @Override
    public FreeListCategory findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public FreeListCategory update(FreeListCategory category) {
        FreeListCategory categoryUpdate = null;
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
        FreeListCategory category = repo.getById(id);
        category.set_deleted(true);
        category.set_activated(false);
        repo.save(category);
    }

    @Override
    public void enabledById(Long id) {
        FreeListCategory category = repo.getById(id);
        category.set_activated(true);
        category.set_deleted(false);
        repo.save(category);
    }



    @Override
    public List<FreeListCategoryDto> getCategoryAndProduct() {
        return repo.getCategoryAndProduct();
    }

}
