package com.microservices.customerservice.service;


import com.microservices.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    public Page<Customer> getAllCustomers(int pageNumber, int pageSize);
}
