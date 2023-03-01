package com.microservices.customerservice.service;

import com.microservices.customerservice.entity.Customer;
import com.microservices.customerservice.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO orderDAO) {
        this.customerDAO = orderDAO;
    }

    @Override
    public  Page<Customer> getAllCustomers(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Customer> customerPage = customerDAO.findAll(page);
        return customerPage;
    }
}
