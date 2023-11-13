package com.ecom.dohsaround.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.FreeListProductDto;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.FreeListProduct;
import com.ecom.dohsaround.repository.FreeListProductRepository;
import com.ecom.dohsaround.service.FreeListService;
import com.ecom.dohsaround.utils.ImageUpload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FreeListServiceImpl implements FreeListService {

    @Autowired
    private FreeListProductRepository freeListProductRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public FreeListProduct save(MultipartFile image, Customer customer, FreeListProductDto freeListProductDto) {

        try {
            FreeListProduct product = new FreeListProduct();
            if (image == null) {
                product.setImage(null);
            } else {
                if (imageUpload.uploadImage(image)) {
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }

            product.setCustomer(customer);
            product.setName(freeListProductDto.getName());
            product.setDescription(freeListProductDto.getDescription());
            product.setCategory(freeListProductDto.getCategory());
            product.setCostPrice(freeListProductDto.getCostPrice());
            product.setCurrentQuantity(freeListProductDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            product.setReported(false);
            product.setCustomerDelete(false);

            return freeListProductRepository.save(product);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<FreeListProductDto> freeListProductPage(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getAllFreeList());
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> freeListProductPageHighToLow(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getHighToLow());
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> freeListProductPageLowToHigh(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getLowToHigh());
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> searchFreelist(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getSearchedProducts(keyword));
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> getProductsInCategory(Long categoryId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getProductsInCategory(categoryId));
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> getProductByCategorySortedHighPrice(Long categoryId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getProductByCategorySortedHighPrice(categoryId));
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<FreeListProductDto> getProductByCategorySortedLowPrice(Long categoryId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.getProductByCategorySortedLowPrice(categoryId));
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public void deleteById(Long id) {
        FreeListProduct product = freeListProductRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        freeListProductRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        FreeListProduct product = freeListProductRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        freeListProductRepository.save(product);
    }

    @Override
    public void reportById(Long id) {
        FreeListProduct product = freeListProductRepository.getById(id);
        product.setReported(true);
        freeListProductRepository.save(product);
    }

    @Override
    public void deleteCustomerProduct(Long id) {
        FreeListProduct product = freeListProductRepository.getById(id);
        product.setCustomerDelete(true);
        freeListProductRepository.save(product);
    }

    @Override
    public List<FreeListProduct> getProductsCustomerUploadedByCustomer(Customer customer) {
        List<FreeListProduct> productList = freeListProductRepository.getProductsByCustomer(customer);
        return productList;
    }

    @Override
    public Page<FreeListProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<FreeListProductDto> products = transfer(freeListProductRepository.findAll());
        Page<FreeListProductDto> productPages = toPage(products, pageable);
        return productPages;
    }


    private List<FreeListProductDto> transfer(List<FreeListProduct> products){
        List<FreeListProductDto> productDtoList = new ArrayList<>();
        for(FreeListProduct product : products){
            FreeListProductDto productDto = new FreeListProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setImage(product.getImage());
            productDto.setDeleted(product.is_deleted());
            productDto.setActivated(product.is_activated());
            productDto.setReported(product.isReported());
            productDto.setCustomer(product.getCustomer());
            productDto.setCustomerDelete(product.isCustomerDelete());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    private Page toPage(List<FreeListProductDto> list , Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    @Override
    public FreeListProduct getProductById(Long id) {
        return freeListProductRepository.getById(id);
    }

    @Override
    public List<FreeListProduct> getRelatedProducts(Long categoryId) {
        return freeListProductRepository.getRelatedProducts(categoryId);
    }

}
