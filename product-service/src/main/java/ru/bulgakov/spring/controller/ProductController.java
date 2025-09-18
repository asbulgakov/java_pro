package ru.bulgakov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bulgakov.spring.dto.product.rq.ProductCreateRq;
import ru.bulgakov.spring.dto.product.rq.ProductUpdateRq;
import ru.bulgakov.spring.dto.product.rs.ProductDtoRs;
import ru.bulgakov.spring.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductDtoRs getProductById(@PathVariable("productId") long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/users/{userId}")
    public List<ProductDtoRs> getProductsByUserId(@PathVariable("userId") long userId) {
        return productService.getProductsByUserId(userId);
    }

    @GetMapping
    public List<ProductDtoRs> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ProductDtoRs createProduct(@RequestBody ProductCreateRq rq) {
        return productService.createProduct(rq);
    }

    @PutMapping
    public ProductDtoRs updateProduct(@RequestBody ProductUpdateRq rq) {
        return productService.updateProduct(rq);
    }

    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable("productId") long id) {
        productService.deleteProduct(id);
    }
}
