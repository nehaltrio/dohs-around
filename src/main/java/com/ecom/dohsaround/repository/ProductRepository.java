package com.ecom.dohsaround.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.Shop;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /*Shop*/
    @Query("select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);

    @Query("select p from Product p where (p.description like %?1% or p.name like %?1%) and p.shop=?2")
    List<Product> searchProductsListByAdmin(String keyword, Shop shop);

    @Query("select p from Product p where (p.description like %?1% or p.name like %?1%) and p.is_activated = true and p.is_deleted = false and p.shop.shopName=?2")
    List<Product> searchProductsListForCustomer(String keyword,String shopName);


    /*Customer*/
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false and p.shop.shopName=?1")
    List<Product> getAllProducts(String shopName);


    @Query(value = "select * from products p where p.is_deleted = false and p.is_activated = true order by rand() asc limit 4 ", nativeQuery = true)
    List<Product> listViewProducts();


    @Query(value = "select * from products p inner join categories c on c.category_id = p.category_id where p.category_id = ?1", nativeQuery = true)
    List<Product> getRelatedProducts(Long categoryId);


    @Query(value = "select p from Product p inner join Category c on c.id = p.category.id where c.id = ?1 and p.is_deleted = false and p.is_activated = true" +
            " and p.shop.shopName=?2")
    List<Product> getProductsInCategory(Long categoryId, String shopName);


    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false and p.shop.shopName=?1" +
            " order by p.costPrice desc")
    List<Product> filterHighPrice(String shopName);


    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false and p.shop.shopName=?1 order by p.costPrice ")
    List<Product> filterLowPrice(String shopName);


    @Query("select p from Product p where p.category.id=?1 and p.is_activated = true and p.is_deleted = false and p.shop.shopName=?2 order by p.costPrice desc")
    List<Product> getProdByCatSortHigh(Long id,String shopName);

    @Query("select p from Product p where p.category.id=?1 and p.is_activated = true and p.is_deleted = false and p.shop.shopName=?2 order by p.costPrice")
    List<Product> getProdByCatSortLow(Long id, String shopName);

    @Query("select p from Product p where p.shop=?1")
    List<Product> findProductByAdmin(Shop shop);

    @Query("select count(p.id) from Product p where p.shop.id=?1")
    int countProductByAdmin_Id(Long id);


}
