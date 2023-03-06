package com.microservices.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservices.productservice.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ListProductDTO {
    @JsonProperty("products")
    private List<ProductDTO> productDTOs = new ArrayList<>();
    @JsonProperty("page_count")
    private int pageCount;
    @JsonProperty("page_number")
    private int pageNumber;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("total_records")
    private long totalRecords;

    public ListProductDTO(List<Product> products, int pageCount, int pageNumber, int pageSize, long totalRecords) {
        ProductDTO productDTO;
        for (Product product: products) {
            productDTO = new ProductDTO(product);
            productDTOs.add(productDTO);
        }
        this.pageCount = pageCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }

    public ListProductDTO(List<Product> products) {
        ProductDTO productDTO;
        for (Product product: products) {
            productDTO = new ProductDTO(product);
            productDTOs.add(productDTO);
        }
    }
}
