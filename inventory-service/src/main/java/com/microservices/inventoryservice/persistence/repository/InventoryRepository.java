package com.microservices.inventoryservice.persistence.repository;

import com.microservices.inventoryservice.persistence.model.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    public List<Inventory> findAllByProductId(Long productId);
}
