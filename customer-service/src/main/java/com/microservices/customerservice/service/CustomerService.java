package com.microservices.customerservice.service;


import com.microservices.customerservice.entity.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> getAllCustomers();
}
