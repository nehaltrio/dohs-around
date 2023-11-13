package com.ecom.dohsaround.service;


import java.util.List;

import com.ecom.dohsaround.dto.FreeListCategoryDto;
import com.ecom.dohsaround.model.FreeListCategory;

public interface FreeListCategoryService {

    List<FreeListCategory> findAll();
    FreeListCategory save(FreeListCategory category);
    FreeListCategory findById(Long id);
    FreeListCategory update(FreeListCategory category);
    void deleteById(Long id);
    void enabledById(Long id);
    List<FreeListCategory> findAllByActivated();


    List<FreeListCategoryDto> getCategoryAndProduct();
}
