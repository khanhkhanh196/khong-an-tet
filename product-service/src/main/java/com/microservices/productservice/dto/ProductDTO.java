package com.microservices.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservices.productservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String vendor;
    @JsonProperty("category_id")
    private int categoryId;
    @JsonProperty("manufactured_year")
    private int manufacturedYear;

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.vendor = product.getVendor();
        this.categoryId = product.getCategoryId();
        this.manufacturedYear = product.getManufacturedYear();
    }
}
