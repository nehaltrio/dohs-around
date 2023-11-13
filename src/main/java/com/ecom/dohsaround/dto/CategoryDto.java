package com.ecom.dohsaround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDto {


    private Long categoryId;
    private String categoryName;
    private Long numberOfProduct;
    private ShopDto admin;

    public CategoryDto(Long categoryId, String categoryName, Long numberOfProduct) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.numberOfProduct = numberOfProduct;
    }
}
