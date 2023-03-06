package com.microservices.inventoryservice.common;

import org.springframework.http.ResponseEntity;

public class ResponseMapper {
	public static <T> ResponseEntity<Response<T>> returnWhenSuccess(T data) {
		return ResponseEntity.ok(new Response<>("success", data, null));
	}
}
