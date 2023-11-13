package com.ecom.dohsaround.dto;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.FreeListCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreeListProductDto {

    private Long id;
    private String name;
    private String description;
    private double costPrice;
    private double salePrice;
    private int currentQuantity;
    private FreeListCategory category;
    private String image;
    private boolean activated;
    private boolean deleted;
    private boolean customerDelete;
    private boolean reported;
    private Customer customer;
    private String postCat;
    

}
