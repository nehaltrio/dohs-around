package com.ecom.dohsaround.service;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.ShopDto;
import com.ecom.dohsaround.model.Shop;

public interface ShopService {
    Shop findByUsername(String username);

    Shop save(ShopDto shopDto);

    void setAdminActive(Long id);
    void setAdminDeactive(Long id);

    Page<ShopDto> pageShops(int pageNo);

    ShopDto findAdminByShopName(String shopName);

    Shop updateShop(MultipartFile imageShop, ShopDto shopDto,Principal principal);
}
