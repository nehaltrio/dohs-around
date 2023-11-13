package com.ecom.dohsaround.service;

import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.model.Product;
import com.ecom.dohsaround.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(Product product, int quantity, Customer customer);

    ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

    ShoppingCart deleteItemFromCart(Product product, Customer customer);



}
