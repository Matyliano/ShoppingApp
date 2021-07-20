package com.example.shopping.controller;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.mapper.ProductMapper;
import com.example.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        return productMapper.toDto(productService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto saveProduct(@RequestBody @Valid ProductDto product){
        return  productMapper.toDto(productService.save(productMapper.toEntity(product)));
    }
    @GetMapping("/products")
    public List<ProductDto> getAllProducts(){
       return productMapper.toListDto(productService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestBody @Valid ProductDto product,@PathVariable Long id){
        return  productMapper.toDto(productService.update(productMapper.toEntity(product), id));
    }

    @GetMapping
    public Page<ProductDto> getProductPage(@RequestParam int page, @RequestParam int size){
        return productService.getPage(PageRequest.of(page, size)).map(productMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteById(id);
    }
}
