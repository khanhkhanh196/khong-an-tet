package com.microservices.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "Gateway alive";
    }
}
 