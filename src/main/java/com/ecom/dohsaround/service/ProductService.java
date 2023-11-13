package com.ecom.dohsaround.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.ProductDto;
import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;

import java.util.List;

public interface ProductService {
    /*Shop*/
    List<ProductDto> findAllByAdmin(Shop shop);
    Product save(MultipartFile[] imageProduct, ProductDto productDto, Shop shop);
    Product update(MultipartFile[] imageProduct, ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);

    Page<ProductDto> pageProducts(int pageNo, Shop shop);
    Page<ProductDto> pageProductsForCustomer(int pageNo,String shopName);


    Page<ProductDto> searchProducts(int pageNo, String keyword, Shop shop);
    Page<ProductDto> searchProductsForCustomer(int pageNo, String keyword, String shopName);


    /*Customer*/
  //  List<Product> getAllProducts();

    List<Product> listViewProducts();

    Product getProductById(Long id);

    List<Product> getRelatedProducts(Long categoryId);

    Page<ProductDto> getProductsInCategory(Long categoryId, int pageNo,String shopName);

    Page<ProductDto> filterHighPrice(int pageNo, String shopName);
    Page<ProductDto> filterLowPrice(int pageNo, String shopName);
    Page<ProductDto> getProductByCategorySortedHighPrice(Long id, int pageNo, String shopName);
    Page<ProductDto> getProductByCategorySortedLowPrice(Long id, int pageNo, String shopName);
}
