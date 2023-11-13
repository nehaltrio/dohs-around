package com.ecom.dohsaround.service;

public interface OrderService {
    void acceptOrderById(Long id);
    void declineOrderById(Long id);
}
