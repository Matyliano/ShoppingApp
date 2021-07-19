package com.example.shopping.mapper.history;


import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.data.history.Revision;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductHistoryMapper {
    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.price", target = "price")
    @Mapping(source = "entity.quantity", target = "quantity")
    @Mapping(source = "requiredRevisionNumber", target = "revisionNumber")
    ProductDto toDto(Revision<Integer, Product> revision);
}
