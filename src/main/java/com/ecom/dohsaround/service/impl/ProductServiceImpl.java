package com.ecom.dohsaround.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.ProductDto;
import com.ecom.dohsaround.model.FileInfo;
import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.ProductRepository;
import com.ecom.dohsaround.service.ProductService;
import com.ecom.dohsaround.utils.ImageUpload;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUpload imageUpload;

    /* Shop */
    @Override
    public List<ProductDto> findAllByAdmin(Shop shop) {
        List<Product> products = productRepository.findProductByAdmin(shop);
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public Product save(MultipartFile[] imageProduct, ProductDto productDto, Shop shop) {
        try {
            Product product = new Product();
            List<FileInfo> fileList = new ArrayList<FileInfo>();
            for (MultipartFile file : imageProduct) {
                String fileContentType = file.getContentType();
                String sourceFileContent = new String(Base64.getEncoder().encodeToString(file.getBytes()));
                String fileName = file.getOriginalFilename();
                FileInfo fileModal = new FileInfo(fileName, sourceFileContent, fileContentType);

                imageUpload.uploadImage(file);
                fileList.add(fileModal);
            }

            product.setListOfAttachedFiles(fileList);
            product.setShop(shop);
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            product.setVideo_url(productDto.getVideo_url());
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Product update(MultipartFile[] imageProduct, ProductDto productDto) {
        try {
            Product product = productRepository.getById(productDto.getId());
          
            if (imageProduct == null) {
                product.setListOfAttachedFiles(productDto.getListOfAttachedFiles());
            } else {
                

                List<FileInfo> fileList = new ArrayList<FileInfo>();
                for (MultipartFile file : imageProduct) {
                    String fileContentType = file.getContentType();
                    String sourceFileContent = new String(Base64.getEncoder().encodeToString(file.getBytes()));
                    String fileName = file.getOriginalFilename();
                    FileInfo fileModal = new FileInfo(fileName, sourceFileContent, fileContentType);
                    imageUpload.uploadImage(file);
                    fileList.add(fileModal);
                }

                product.setListOfAttachedFiles(fileList);
            }



            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            product.setVideo_url(productDto.getVideo_url());
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setListOfAttachedFiles(product.getListOfAttachedFiles());
        productDto.setDeleted(product.is_deleted());
        productDto.setActivated(product.is_activated());
        productDto.setVideo_url(product.getVideo_url());
        return productDto;
    }

    @Override
    public Page<ProductDto> pageProducts(int pageNo, Shop shop) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.findProductByAdmin(shop));
        Page<ProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> pageProductsForCustomer(int pageNo, String shopName) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.getAllProducts(shopName));
        Page<ProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword, Shop shop) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> productDtoList = transfer(productRepository.searchProductsListByAdmin(keyword, shop));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    @Override
    public Page<ProductDto> searchProductsForCustomer(int pageNo, String keyword, String shopName) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> productDtoList = transfer(productRepository.searchProductsListForCustomer(keyword, shopName));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    private Page toPage(List<ProductDto> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transfer(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setListOfAttachedFiles(product.getListOfAttachedFiles());
            productDto.setDeleted(product.is_deleted());
            productDto.setActivated(product.is_activated());
            productDto.setVideo_url(product.getVideo_url());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    /* Customer */

    // @Override
    // public List<Product> getAllProducts() {
    // return productRepository.getAllProducts();
    // }

    @Override
    public List<Product> listViewProducts() {
        return productRepository.listViewProducts();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Product> getRelatedProducts(Long categoryId, String shopName) {
        return productRepository.getProductsInCategory(categoryId, shopName);
    }

    @Override
    public Page<ProductDto> getProductsInCategory(Long categoryId, int pageNo, String shopName) {

        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.getProductsInCategory(categoryId, shopName));
        Page<ProductDto> productPages = toPage(products, pageable);

        return productPages;
    }

    @Override
    public Page<ProductDto> filterHighPrice(int pageNo, String shopName) {

        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.filterHighPrice(shopName));
        Page<ProductDto> productPages = toPage(products, pageable);

        return productPages;
    }

    @Override
    public Page<ProductDto> filterLowPrice(int pageNo, String shopName) {

        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.filterLowPrice(shopName));
        Page<ProductDto> productPages = toPage(products, pageable);

        return productPages;
    }

    @Override
    public Page<ProductDto> getProductByCategorySortedHighPrice(Long id, int pageNo, String shopName) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.getProdByCatSortHigh(id, shopName));
        Page<ProductDto> productPages = toPage(products, pageable);

        return productPages;
    }

    @Override
    public Page<ProductDto> getProductByCategorySortedLowPrice(Long id, int pageNo, String shopName) {
        Pageable pageable = PageRequest.of(pageNo, 9);
        List<ProductDto> products = transfer(productRepository.getProdByCatSortLow(id, shopName));
        Page<ProductDto> productPages = toPage(products, pageable);

        return productPages;
    }

}
