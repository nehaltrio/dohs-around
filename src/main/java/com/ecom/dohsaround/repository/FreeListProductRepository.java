package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.FreeListProduct;

import java.util.List;

public interface FreeListProductRepository extends JpaRepository<FreeListProduct,Long> {

    @Query("select p from FreeListProduct p where p.is_activated = true and p.is_deleted = false  and p.customerDelete = false")
    List<FreeListProduct> getAllFreeList();


    @Query("select p from FreeListProduct p where p.is_activated = true and p.is_deleted = false  and p.customerDelete = false" +
            " order by p.costPrice desc")
    List<FreeListProduct> getHighToLow();

    @Query("select p from FreeListProduct p where p.is_activated = true and p.is_deleted = false  and p.customerDelete = false" +
            " order by p.costPrice")
    List<FreeListProduct> getLowToHigh();

    @Query("select p from FreeListProduct p where (p.description like %?1% or p.name like %?1%) and p.is_activated = true and p.is_deleted = false  and p.customerDelete = false")
    List<FreeListProduct> getSearchedProducts(String keyword);

    @Query(value = "select p from FreeListProduct p inner join FreeListCategory c on c.id = p.category.id where c.id = ?1 and p.is_deleted = false and p.is_activated = true  and p.customerDelete = false")
    List<FreeListProduct> getProductsInCategory(Long categoryId);

    @Query("select p from FreeListProduct p where p.category.id=?1 and p.is_activated = true and p.is_deleted = false  and p.customerDelete = false order by p.costPrice desc")
    List<FreeListProduct> getProductByCategorySortedHighPrice(Long categoryId);

    @Query("select p from FreeListProduct p where p.category.id=?1 and p.is_activated = true and p.is_deleted = false  and p.customerDelete = false order by p.costPrice")
    List<FreeListProduct> getProductByCategorySortedLowPrice(Long categoryId);

    @Query("select p from FreeListProduct p where p.is_activated = true and p.is_deleted = false and p.customerDelete = false and p.customer=?1")
    List<FreeListProduct> getProductsByCustomer(Customer customer);

    //write query for getting related products
    @Query(value = "select * from freelist_item p inner join freelist_categories c on c.category_id = p.category_id where p.category_id = ?1 and p.is_deleted = false and p.is_activated = true  and p.customer_delete = false", nativeQuery = true)
    List<FreeListProduct> getRelatedProducts(Long categoryId);
      
}
