package com.microservices.productservice.model;

import com.microservices.productservice.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "category_id")
    private int categoryId;
    @Column(name = "manufactured_year")
    private Integer manufacturedYear;

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.vendor = productDTO.getVendor();
        this.categoryId = productDTO.getCategoryId();
        this.manufacturedYear = productDTO.getManufacturedYear();
    }
}
