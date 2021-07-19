package com.example.shopping.mapper;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.Product;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {


    public Product toEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();
    }


    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }


    public List<ProductDto> toListDto(List<Product> products) {
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }
}
