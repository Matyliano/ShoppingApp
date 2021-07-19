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

    @Transactional
    public Product update(Product product, Long id) {
        Product productDb = findById(id);
        productDb.setId(product.getId());
        productDb.setName(product.getName());
        productDb.setPrice(product.getPrice());
        productDb.setQuantity(product.getQuantity());
        return productDb;
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public Page<Product> getPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
