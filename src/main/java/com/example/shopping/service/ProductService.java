package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    public final ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        log.info("Product not in cache id:{}", id);
        return productRepository.findAll().stream()
                .filter(element -> element.getId().equals(id)).findFirst();
    }

    public Optional<Product> addProduct(Product product) {
        boolean isProductExists = productRepository.findAll().
                stream().anyMatch(element -> element.getName().equals(product.getName()));
        return isProductExists ? Optional.empty() : Optional.of(save(product));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Transactional
    public Optional<Product> update(Product product, Long id) {
        return productRepository.findAll().stream()
                .filter(element -> element.getId().equals(id))
                .findFirst()
                .map(element -> {
                    element.setPrice(product.getPrice());
                    element.setName(product.getName());
                    element.setCategory(product.getCategory());
                    element.setQuantity(product.getQuantity());
                    element.setLastModifiedBy(product.getLastModifiedBy());
                    element.setLastModifiedDate(product.getLastModifiedDate());
                    return element;
                });
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public Page<Product> getPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
