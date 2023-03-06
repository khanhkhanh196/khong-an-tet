package com.microservices.inventoryservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
	private String status;
	private T data;
	private String message;
}
