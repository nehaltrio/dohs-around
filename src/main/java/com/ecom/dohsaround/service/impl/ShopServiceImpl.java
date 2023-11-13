package com.ecom.dohsaround.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.dohsaround.dto.ShopDto;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.RoleRepository;
import com.ecom.dohsaround.repository.ShopRepository;
import com.ecom.dohsaround.service.ShopService;
import com.ecom.dohsaround.utils.ImageUpload;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public Shop findByUsername(String username) {
        return shopRepository.findByUsername(username);
    }

    @Override
    public Shop save(ShopDto shopDto) {
        Shop shop = new Shop();
        shop.setFirstName(shopDto.getFirstName());
        shop.setLastName(shopDto.getLastName());
        shop.setUsername(shopDto.getUsername());
        shop.setPassword(shopDto.getPassword());
        shop.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        shop.setShopName(shopDto.getShopName());
        shop.setActive(true);
        shop.setShopCategory(shopDto.getShopCategory());
        shop.setDeliveryLocation(shopDto.getDeliveryLocation());

        return shopRepository.save(shop);
    }

    @Override
    public void setAdminActive(Long id) {
        Shop shop = shopRepository.getById(id);
        shop.setActive(true);
        shopRepository.save(shop);
    }

    @Override
    public void setAdminDeactive(Long id) {
        Shop shop = shopRepository.getById(id);
        shop.setActive(false);
        shopRepository.save(shop);
    }

    @Override
    public Page<ShopDto> pageShops(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ShopDto> shopDtoList = transfer(shopRepository.findAll());
        Page<ShopDto> adminDtoPage = toPage(shopDtoList, pageable);
        return adminDtoPage;
    }

    @Override
    public ShopDto findAdminByShopName(String shopName) {
        Shop shop = shopRepository.findAdminByShopName(shopName);
        ShopDto shopDto = new ShopDto();
        shopDto.setId(shop.getId());
        shopDto.setShopName(shop.getShopName());
        shopDto.setFirstName(shop.getFirstName());
        shopDto.setLastName(shop.getLastName());
        shopDto.setActive(shop.isActive());
        shopDto.setShopAddress(shop.getShopAddress());
        shopDto.setShopDesc(shop.getShopDesc());
        shopDto.setShopFbURL(shop.getShopFbURL());
        shopDto.setShopInstaURL(shop.getShopInstaURL());
        shopDto.setShopGmapURL(shop.getShopGmapURL());
        shopDto.setImage(shop.getImage());
        shopDto.setShopPhone(shop.getShopPhone());
        shopDto.setShopCategory(shop.getShopCategory());
        shopDto.setDeliveryLocation(shop.getDeliveryLocation());

        return shopDto;
    }

    @Override
    public Shop updateShop(MultipartFile imageShop, ShopDto shopDto, Principal principal) {
        try {
            Shop shop = shopRepository.findByUsername(principal.getName());
            System.out.println(shopDto.getUsername());
            if (imageShop == null) {
                shop.setImage(shopDto.getImage());
            } else {
                if (!imageUpload.checkExisted(imageShop)) {
                    imageUpload.uploadImage(imageShop);
                }

                String sourceFileContent = new String(Base64.getEncoder().encodeToString(imageShop.getBytes()));

                shop.setImage(sourceFileContent);
            }

            shop.setShopName(shopDto.getShopName());
            shop.setShopAddress(shopDto.getShopAddress());
            shop.setShopDesc(shopDto.getShopDesc());
            shop.setShopFbURL(shopDto.getShopFbURL());
            shop.setShopInstaURL(shopDto.getShopInstaURL());
            shop.setShopGmapURL(shopDto.getShopGmapURL());
           
            shop.setShopPhone(shopDto.getShopPhone());
            shop.setDeliveryLocation(shopDto.getDeliveryLocation());
            return shopRepository.save(shop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Page toPage(List<ShopDto> list, Pageable pageable) {
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


    private List<ShopDto> transfer(List<Shop> shops) {
        List<ShopDto> shopDtoList = new ArrayList<>();
        for (Shop shop : shops) {
            ShopDto shopDto = new ShopDto();

            shopDto.setId(shop.getId());
            shopDto.setShopName(shop.getShopName());
            shopDto.setActive(shop.isActive());
            shopDto.setFirstName(shop.getFirstName());
            shopDto.setUsername(shop.getUsername());
            shopDto.setShopCategory(shop.getShopCategory());
            shopDto.setDeliveryLocation(shop.getDeliveryLocation());


            shopDtoList.add(shopDto);
        }
        return shopDtoList;
    }

}
