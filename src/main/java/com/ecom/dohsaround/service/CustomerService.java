package com.ecom.dohsaround.service;


import org.springframework.data.domain.Page;

import com.ecom.dohsaround.dto.CustomerDto;
import com.ecom.dohsaround.model.Customer;

public interface CustomerService {

    CustomerDto save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer saveInfor(Customer customer);

    void setCustomerActive(Long id);
    void setCustomerDeactivate(Long id);

    Page<CustomerDto> pageCustomers(int pageNo);
}
