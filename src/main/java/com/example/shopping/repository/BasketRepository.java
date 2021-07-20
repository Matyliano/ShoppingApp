package com.example.shopping.repository;

import com.example.shopping.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    void deleteByProductIdAndUserId(Long productId, Long userId);
    List<Basket> findByUserId(Long userId);
    Optional<Basket> findByProductIdAndUserId(Long productId, Long userId);
}
