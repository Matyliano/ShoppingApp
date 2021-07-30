package com.example.shopping.dto;

import com.example.shopping.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto{

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    @Positive
    private Double quantity;
    private Integer revisionNumber;
}
