package com.ecom.dohsaround.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.FreeListProductDto;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.FreeListProduct;

import java.util.List;

public interface FreeListService {
     FreeListProduct save(MultipartFile image, Customer customer, FreeListProductDto freeListProductDto);
     Page<FreeListProductDto> freeListProductPage(int pageNo);
     Page<FreeListProductDto> freeListProductPageHighToLow(int pageNo);
     Page<FreeListProductDto> freeListProductPageLowToHigh(int pageNo);

     Page<FreeListProductDto> searchFreelist(int pageNo, String keyword);

    Page<FreeListProductDto> getProductsInCategory(Long categoryId, int pageNo);

    Page<FreeListProductDto> getProductByCategorySortedHighPrice(Long categoryId, int pageNo);

    Page<FreeListProductDto> getProductByCategorySortedLowPrice(Long categoryId, int pageNo);

    void deleteById(Long id);
    void enableById(Long id);

    void reportById(Long id);
    void deleteCustomerProduct(Long id);

    List<FreeListProduct> getProductsCustomerUploadedByCustomer(Customer customer);

    Page<FreeListProductDto> pageProducts(int pageNo);

    FreeListProduct getProductById(Long id);
    List<FreeListProduct> getRelatedProducts(Long categoryId);
}
