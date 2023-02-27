package com.microservices.orderservice.service;

import com.microservices.orderservice.entity.Order;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders(); 
}
