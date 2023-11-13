package com.ecom.dohsaround.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecom.dohsaround.dto.CustomerDto;
import com.ecom.dohsaround.model.Customer;
import com.ecom.dohsaround.repository.CustomerRepository;
import com.ecom.dohsaround.repository.RoleRepository;
import com.ecom.dohsaround.service.CustomerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto save(CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(repository.findByName("CUSTOMER")));

        Customer customerSave = customerRepository.save(customer);
        return mapperDTO(customerSave);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer saveInfor(Customer customer) {
        Customer customer1 = customerRepository.findByUsername(customer.getUsername());
        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setCountry(customer.getCountry());
        customer1.setPhoneNumber(customer.getPhoneNumber());
        return customerRepository.save(customer1);
    }

    @Override
    public void setCustomerActive(Long id) {
        Customer customer = customerRepository.getById(id);
        customer.setActive(true);
        customerRepository.save(customer);
    }

    @Override
    public void setCustomerDeactivate(Long id) {
        Customer customer = customerRepository.getById(id);
        customer.setActive(false);
        customerRepository.save(customer);
    }

    @Override
    public Page<CustomerDto> pageCustomers(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<CustomerDto> customerDtoList = transfer(customerRepository.findAll());
        Page<CustomerDto> customerDtos = toPage(customerDtoList, pageable);
        return customerDtos;
    }


    private Page toPage(List<CustomerDto> list , Pageable pageable){
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


    private List<CustomerDto> transfer(List<Customer> customers){
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for(Customer customer : customers){
            CustomerDto customerDto = new CustomerDto();

            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setLastName(customer.getLastName());
            customerDto.setPassword(customer.getPassword());
            customerDto.setUsername(customer.getUsername());
            customerDto.setPhoneNumber(customer.getPhoneNumber());
            customerDto.setAddress(customer.getAddress());
            customerDto.setActive(customer.isActive());

            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }


    private CustomerDto mapperDTO(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setUsername(customer.getUsername());
        return customerDto;
    }
}
