package com.microservices.customerservice.controller;

import com.microservices.customerservice.entity.Customer;
import com.microservices.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-customer")
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService orderService) {
        this.customerService = orderService;
    }

    @GetMapping("/customers/{pageNumber}/{pageSize}")
    public Page<Customer> getCustomers(@PathVariable int pageNumber, @PathVariable int pageSize ) {
        return customerService.getAllCustomers(pageNumber, pageSize);
    }
}
