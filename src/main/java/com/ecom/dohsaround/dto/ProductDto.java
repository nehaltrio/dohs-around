package com.ecom.dohsaround.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.ecom.dohsaround.model.Category;
import com.ecom.dohsaround.model.FileInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double costPrice;
    private double salePrice;
    private int currentQuantity;
    private Category category;
    private List<FileInfo> listOfAttachedFiles;
    private boolean activated;
    private boolean deleted;
    private String video_url;
}
