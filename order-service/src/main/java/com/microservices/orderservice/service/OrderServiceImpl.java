package com.microservices.orderservice.service;

import com.microservices.orderservice.dao.OrderDAO;
import com.microservices.orderservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDAO orderDAO;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }
}
