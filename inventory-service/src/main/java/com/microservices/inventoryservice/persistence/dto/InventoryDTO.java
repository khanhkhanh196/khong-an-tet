package com.microservices.inventoryservice.persistence.dto;

import lombok.Data;

@Data
public class InventoryDTO {
	private Long productId;

	private Integer quantity = 0;

	private Integer sold = 0;
}
