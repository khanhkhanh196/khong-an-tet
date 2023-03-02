package com.microservices.productservice.service;

import com.microservices.productservice.dto.ListProductDTO;
import com.microservices.productservice.dto.ProductDTO;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.repository.CustomProductRepository;

import com.microservices.productservice.repository.ProductRepository;
import com.microservices.productservice.specification.Filter;
import com.microservices.productservice.specification.QueryOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ctc.wstx.shaded.msv_core.datatype.xsd.NumberType.save;

@Service
public class ProductService {
//    @Autowired
//    private ProductMongoRepository productMongoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomProductRepository customProductRepository;

    public ProductResponse<ListProductDTO> findAllProducts(int page, int size, ProductDTO productDTO) {
        Pageable pageRequest = PageRequest.of(page, size);
        ListProductDTO dto;
        if (Objects.isNull(productDTO) || (!Objects.isNull(productDTO) && Objects.isNull(productDTO.getName()) && Objects.isNull(productDTO.getVendor()) && Objects.isNull(productDTO.getManufacturedYear()))) {
            Page<Product> products = productRepository.findAll(pageRequest);
            dto = new ListProductDTO(products.getContent(), products.getTotalPages(), products.getNumber(),
                    products.getSize(), products.getTotalElements());
        }

        else {
            String name = productDTO.getName();
            String vendor = productDTO.getVendor();
            int manufacturedYear = productDTO.getManufacturedYear();

            List<Filter> filters = new ArrayList<>();

            if (!Objects.isNull(name)) {
                Filter nameLike = Filter.builder()
                        .field("name")
                        .operator(QueryOperator.LIKE)
                        .value(name)
                        .build();
                filters.add(nameLike);
            }
            if (!Objects.isNull(vendor)) {
                Filter vendorLike = Filter.builder()
                        .field("vendor")
                        .operator(QueryOperator.LIKE)
                        .value(vendor)
                        .build();
                filters.add(vendorLike);
            }
            if (manufacturedYear > 0) {
                Filter manufacturedYearLike = Filter.builder()
                        .field("manufacturedYear")
                        .operator(QueryOperator.EQUALS)
                        .value(String.valueOf(manufacturedYear))
                        .build();
                filters.add(manufacturedYearLike);
            }

            List<Product> products = customProductRepository.getQueryResult(filters);
            dto = new ListProductDTO(products);
        }

        return new ProductResponse<>(dto, "", null);
    }

    public Product findOneProduct(int id) throws Exception {
        return productRepository.findById(id).orElseThrow(() -> new Exception());
    }

    public ProductResponse<ProductDTO> createOneProduct(ProductDTO productDTO) {
        Product tmp = new Product(productDTO);
        Product product = productRepository.save(tmp);
        ProductResponse<ProductDTO> response = new ProductResponse<>(productDTO, "", null);
        return response;
    }

    public ProductResponse<ProductDTO> updateOneProduct(Integer id, ProductDTO productDTO) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new Exception());
        product.setName(Objects.requireNonNullElse(productDTO.getName(), product.getName()));
        product.setVendor(Objects.requireNonNullElse(productDTO.getVendor(), product.getVendor()));
        product.setCategoryId((productDTO.getCategoryId() != 0) ? product.getCategoryId() : product.getCategoryId());
        product.setManufacturedYear(product.getManufacturedYear());
        product.setManufacturedYear(Objects.requireNonNullElse(productDTO.getManufacturedYear(), product.getManufacturedYear()));
        productRepository.save(product);
        ProductResponse<ProductDTO> response = new ProductResponse<>(productDTO, "", null);
        return response;
    }

    public ProductResponse<ProductDTO> deleteOneProduct(Integer id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() -> new Exception());
        ProductDTO productDTO = new ProductDTO(product);
        productRepository.delete(product);
        return new ProductResponse<>(productDTO, "", null);
    }




}
