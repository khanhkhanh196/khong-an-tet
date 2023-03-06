package com.microservices.productservice.controller;

import com.microservices.productservice.dto.ListProductDTO;
import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "Product alive";
    }

    @PostMapping("/")
    public ResponseEntity createOneProduct(@RequestBody ProductDTO productDTO) {
        ProductResponse<ProductDTO> response = productService.createOneProduct(productDTO);
        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity findAllProducts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size,
                                          @RequestBody(required = false) ProductDTO productDTO) {
        ProductResponse<ListProductDTO> response = productService.findAllProducts(page, size, productDTO);
        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateOneProduct(@PathVariable("id") Integer id, @RequestBody ProductDTO productDTO) throws Exception {
        Product product = productService.findOneProduct(id);
        ProductResponse<ProductDTO> response = productService.updateOneProduct(id, productDTO);
        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOneProduct(@PathVariable("id") Integer id) throws Exception {
        Product product = productService.findOneProduct(id);
        ProductResponse response = productService.deleteOneProduct(id);
        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
