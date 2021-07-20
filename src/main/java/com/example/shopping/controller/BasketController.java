package com.example.shopping.controller;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.mapper.ProductMapper;
import com.example.shopping.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final ProductMapper productMapper;

    @PostMapping("/add")
    public void addBasket(@RequestBody @Valid ProductDto productDto){
        basketService.addToBasket(productMapper.toEntity(productDto));
    }

    @PutMapping("/update")
    public void updateBasket(@RequestBody @Valid ProductDto productDto){
        basketService.updateProductQuantityInBasket(productMapper.toEntity(productDto));
    }

    @DeleteMapping("/{productId}")
    public void deleteBasketById(@PathVariable Long productId){
        basketService.removeProductFromBasket(productId);
    }

    @GetMapping
    public List<ProductDto> getBasket(){
        return productMapper.toListDto(basketService.getBasket());
    }
}
