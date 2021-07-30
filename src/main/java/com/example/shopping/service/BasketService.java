package com.example.shopping.service;

import com.example.shopping.entity.Basket;
import com.example.shopping.entity.Product;
import com.example.shopping.exception.QuantityNotEnoughException;
import com.example.shopping.repository.BasketRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final UserServiceImpl userService;
    private final ProductService productService;

    public BasketService(BasketRepository basketRepository, UserServiceImpl userService, ProductService productService) {
        this.basketRepository = basketRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public Basket addToBasket(Product product) {
        Optional<Product> productDb = productService.findById(product.getId());

        if (productDb.isPresent() && productDb.get().getQuantity() <= product.getQuantity()) {
            throw new QuantityNotEnoughException("Not enough quantity of product in database");
        }

        return basketRepository.save(Basket.builder()
                .quantity(product.getQuantity())
                .product(productDb.get())
                .user(userService.getCurrentUser())
                .build());
    }

    @Transactional
    public void removeProductFromBasket(Long productId) {
        basketRepository.deleteByProductIdAndUserId(productId, userService.getCurrentUser().getId());
    }

    public Basket updateProductQuantityInBasket(Product product) {
        var currentUser = userService.getCurrentUser();
        basketRepository.findByProductIdAndUserId(product.getId(),currentUser.getId())
                .ifPresentOrElse(basket -> {
                    if(product.getQuantity() <= productService.findById(product.getId()).get().getQuantity()){
                        basket.setQuantity(product.getQuantity());
                        basketRepository.save(basket);
                    }
                    throw new QuantityNotEnoughException("Not enough quantity to increase");
                }, () -> addToBasket(product));
        return basketRepository.findByProductIdAndUserId(product.getId(), currentUser.getId()).orElseThrow();
    }

    public List<Product> getBasket() {
        Long userId = userService.getCurrentUser().getId();

        return basketRepository.findByUserId(userId).stream()
                .map(basket -> {
                    var product = basket.getProduct();
                    product.setQuantity(basket.getQuantity());
                    return product;
                })
                .collect(Collectors.toList());
    }
}
