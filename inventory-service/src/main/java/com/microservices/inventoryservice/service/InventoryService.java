package com.microservices.inventoryservice.service;

import com.microservices.inventoryservice.persistence.dto.InventoryDTO;
import com.microservices.inventoryservice.persistence.model.Inventory;
import com.microservices.inventoryservice.persistence.repository.InventoryRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class InventoryService {
    public final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory getInventoryByProductId(Long productId) {
        List<Inventory> inventories = inventoryRepository.findAllByProductId(productId);
        if (inventories.isEmpty())
            return null;
        else return inventories.get(0);
    }

    public Page<Inventory> getAllInventories(Integer page, Integer pageSize) {
		return inventoryRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Inventory saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
        return inventoryRepository.findById(inventory.getInventoryId()).get();
    }

    public Inventory saveInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setSold(inventoryDTO.getSold());

        inventoryRepository.save(inventory);
        return inventoryRepository.findById(inventory.getInventoryId()).get();
    }

    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }
}

