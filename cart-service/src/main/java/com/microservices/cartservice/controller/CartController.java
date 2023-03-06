package com.microservices.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final WebClient.Builder webClientBuilder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "Cart alive";
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String getStatusFromOtherService() {
        webClientBuilder.build().get()
                .uri("http://product-service/api/products")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "Cart alive";
    }
}
