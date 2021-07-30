package com.example.shopping.controller;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.Product;
import com.example.shopping.mapper.ProductMapper;
import com.example.shopping.service.ProductService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> productOptional = productService.findById(id);
        return productOptional.map(product -> {
            addLinkToProduct(product);
            return ResponseEntity.ok(product);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> addNewProduct(@RequestBody ProductDto productDTO) {
        Optional<Product> addedProduct = productService.addProduct(productMapper.toEntity(productDTO));
        return addedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "Pobierz wszystkie produkty")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "W przypadku zapytania poprawnego zwracany jest status code 200 i lista wszystkich produktów."),
            @ApiResponse(code = 404, message = "W przypadku braku danych  " +
                    "zwracany jest komunikat 404 Not Found"),
            @ApiResponse(code = 400, message = "W przypadku zadania nieprawidłowo sformułowanych zapytań serwis zwraca" +
                    " komunikat 400 Bad Request")
    })

    @GetMapping("/products")
    public ResponseEntity<CollectionModel<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        products.forEach(product -> product.addIf(!product.hasLinks(), () -> linkTo(ProductController.class)
                .slash(product.getId()).withSelfRel()));
        var link = linkTo(ProductController.class).withSelfRel();
        return ResponseEntity.ok(new CollectionModel<>(products, link));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody @Valid ProductDto product,@PathVariable Long id){
        Optional<Product> productUpdatedOptional = productService.update(productMapper.toEntity(product),id);
        return productUpdatedOptional.map(productUpdated -> {
            addLinkToProduct(productUpdated);
            return ResponseEntity.ok(productUpdated);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public Page<ProductDto> getProductPage(@RequestParam int page, @RequestParam int size){
        return productService.getPage(PageRequest.of(page, size)).map(productMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteById(id);
    }

    private void addLinkToProduct(Product product) {
        product.add(linkTo(ProductController.class).slash(product.getId()).withSelfRel());
    }
}
