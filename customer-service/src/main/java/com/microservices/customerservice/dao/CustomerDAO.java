package com.microservices.customerservice.dao;

import com.microservices.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends PagingAndSortingRepository<Customer, Integer> {
}
