package com.example.shopping.controller.history;


import com.example.shopping.dto.ProductDto;
import com.example.shopping.mapper.history.ProductHistoryMapper;
import com.example.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history/products")
@RequiredArgsConstructor
public class ProductHistoryController {
    private final ProductHistoryMapper productHistoryMapper;
    private final ProductRepository productRepository;

    @GetMapping("/{id}")
    public Page<ProductDto> getProductHistoryPage(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return productRepository.findRevisions(id, PageRequest.of(page, size)).map(productHistoryMapper::toDto);
    }
}
