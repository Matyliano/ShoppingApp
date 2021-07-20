package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    public final ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        log.info("Product not in cache id:{}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with provided id: " + id + " doesn't exist"));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Transactional
    public Product update(Product product, Long id) {
        var productDatabase = findById(id);
        productDatabase.setId(product.getId());
        productDatabase.setName(product.getName());
        productDatabase.setPrice(product.getPrice());
        productDatabase.setQuantity(product.getQuantity());
        return productDatabase;
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public Page<Product> getPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
