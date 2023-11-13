package com.ecom.dohsaround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FreeListCategoryDto {


    private Long categoryId;
    private String categoryName;
    private Long numberOfProduct;

}
