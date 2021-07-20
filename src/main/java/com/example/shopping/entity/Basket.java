package com.example.shopping.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PositiveOrZero;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @PositiveOrZero
    private Double quantity;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
}
