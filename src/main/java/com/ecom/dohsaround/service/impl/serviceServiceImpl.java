package com.ecom.dohsaround.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ecom.dohsaround.dto.ServiceDto;
import com.ecom.dohsaround.model.Service;
import com.ecom.dohsaround.model.Shop;
import com.ecom.dohsaround.repository.serviceRepository;
import com.ecom.dohsaround.service.serviceService;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class serviceServiceImpl implements serviceService {

    @Autowired
    private serviceRepository serviceRepository;

    @Override
    public Page<ServiceDto> pageServices(int pageNo, Shop shop) {
        Pageable pageable = PageRequest.of(pageNo, 100);
        List<ServiceDto> services = transfer(serviceRepository.findServiceByShop(shop));
        Page<ServiceDto> servicePages = toPage(services, pageable);
        return servicePages;
    }

    @Override
    public Page<ServiceDto> searchService(int pageNo, String keyword, Shop shop) {
        Pageable pageable = PageRequest.of(pageNo, 100);
        List<ServiceDto> services = transfer(serviceRepository.searchServiceByShop(keyword,shop));
        Page<ServiceDto> servicePages = toPage(services, pageable);
        return servicePages;
    }

    @Override
    public Service save(ServiceDto serviceDto, Shop shop) {
        Service service = new Service();

        service.setShop(shop);
        service.setServiceName(serviceDto.getServiceName());
        service.setServiceFee(serviceDto.getServiceFee());
        service.setDescription(serviceDto.getDescription());
        service.setActive(true);

        return serviceRepository.save(service);
    }

    @Override
    public ServiceDto getById(Long id) {
        Service service = serviceRepository.getById(id);
        ServiceDto serviceDto = new ServiceDto();

        serviceDto.setId(service.getId());
        serviceDto.setServiceName(service.getServiceName());
        serviceDto.setDescription(service.getDescription());
        serviceDto.setServiceFee(service.getServiceFee());
        serviceDto.setActive(service.isActive());

        return serviceDto;
    }

    @Override
    public Service update(ServiceDto serviceDto) {
        Service service = serviceRepository.getById(serviceDto.getId());

        service.setServiceName(serviceDto.getServiceName());
        service.setServiceFee(serviceDto.getServiceFee());
        service.setDescription(serviceDto.getDescription());
        service.setActive(true);

        return serviceRepository.save(service);
    }

    @Override
    public void enableById(Long id) {
        Service service = serviceRepository.getById(id);
        service.setActive(true);
        serviceRepository.save(service);
    }

    @Override
    public void deleteById(Long id) {
        Service service = serviceRepository.getById(id);
        service.setActive(false);
        serviceRepository.save(service);
    }


    private Page toPage(List<ServiceDto> list , Pageable pageable){
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


    private List<ServiceDto> transfer(List<Service> services){
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for(Service service : services){
            ServiceDto serviceDto = new ServiceDto();
            serviceDto.setId(service.getId());
            serviceDto.setServiceName(service.getServiceName());
            serviceDto.setDescription(service.getDescription());
            serviceDto.setServiceFee(service.getServiceFee());
            serviceDto.setActive(service.isActive());

            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }
}
