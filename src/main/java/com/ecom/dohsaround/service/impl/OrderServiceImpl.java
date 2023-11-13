package com.ecom.dohsaround.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.dohsaround.model.Order;
import com.ecom.dohsaround.repository.OrderRepository;
import com.ecom.dohsaround.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void acceptOrderById(Long id) {
        Order order = orderRepository.getById(id);
        order.setOrderStatus(true);
        orderRepository.save(order);
    }

    @Override
    public void declineOrderById(Long id) {
        Order order = orderRepository.getById(id);
        order.setOrderStatus(false);
        orderRepository.save(order);
    }
}
