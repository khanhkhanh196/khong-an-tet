package com.microservices.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "Product alive";
    }
}
