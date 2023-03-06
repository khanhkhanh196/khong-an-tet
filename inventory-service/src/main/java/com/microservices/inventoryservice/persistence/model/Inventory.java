package com.microservices.inventoryservice.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "inventory")
@NoArgsConstructor
public class Inventory {
    @Id
	@NotNull
	@Column(name = "inventory_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long inventoryId;

	@NotNull
	@Column(name = "product_id")
	private Long productId;

	@NotNull
	@Column(name = "quantity")
	private Integer quantity;

	@NotNull
	@Column(name = "sold")
	private Integer sold;
}
