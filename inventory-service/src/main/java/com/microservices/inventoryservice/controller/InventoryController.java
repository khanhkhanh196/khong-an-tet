package com.microservices.inventoryservice.controller;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.inventoryservice.common.ResponseMapper;
import com.microservices.inventoryservice.common.Response;
import com.microservices.inventoryservice.persistence.model.Inventory;
import com.microservices.inventoryservice.persistence.dto.InventoryDTO;
import com.microservices.inventoryservice.service.InventoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class InventoryController {

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "Inventory alive";
    }

    @Autowired
	private InventoryService InventoryService;

	@PostMapping
	public ResponseEntity<Response<Inventory>> createInventory(@RequestBody InventoryDTO inventoryDTO) {
		return ResponseMapper.returnWhenSuccess(InventoryService.saveInventory(inventoryDTO));
	}

    @PutMapping
	public ResponseEntity<Response<Inventory>> updateInventory(@RequestBody Inventory inventory) {
		return ResponseMapper.returnWhenSuccess(InventoryService.saveInventory(inventory));
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Response<Inventory>> deleteInventoryById(@PathVariable Long inventoryId) {
		InventoryService.deleteInventory(inventoryId);
		return ResponseMapper.returnWhenSuccess((Inventory) null);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<Response<Inventory>> findInventoryById(@PathVariable Long inventoryId) throws IOException {
		return ResponseMapper.returnWhenSuccess(InventoryService.getInventoryByProductId(inventoryId));
	}

	@GetMapping("/")
	public ResponseEntity<Response<Page<Inventory>>> getAllInventories(@RequestParam Integer page, @RequestParam Integer pageSize) throws IOException {
		return ResponseMapper.returnWhenSuccess(InventoryService.getAllInventories(page, pageSize));
	}
}
