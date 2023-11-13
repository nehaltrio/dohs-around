package com.ecom.dohsaround.service;

import org.springframework.data.domain.Page;

import com.ecom.dohsaround.dto.ServiceDto;
import com.ecom.dohsaround.model.Service;
import com.ecom.dohsaround.model.Shop;


public interface serviceService {
    Page<ServiceDto> pageServices(int pageNo, Shop shop);

    Page<ServiceDto> searchService(int pageNo, String keyword, Shop shop);

    Service save(ServiceDto serviceDto, Shop shop);

    ServiceDto getById(Long id);

    Service update(ServiceDto serviceDto);

    void enableById(Long id);

    void deleteById(Long id);
}
