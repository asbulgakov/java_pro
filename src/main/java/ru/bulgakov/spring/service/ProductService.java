package ru.bulgakov.spring.service;

import ru.bulgakov.spring.dto.product.rq.ProductCreateRq;
import ru.bulgakov.spring.dto.product.rq.ProductUpdateRq;
import ru.bulgakov.spring.dto.product.rs.ProductDtoRs;

import java.util.List;

public interface ProductService {
    List<ProductDtoRs> getProductsByUserId(Long userId);
    ProductDtoRs getProductById(Long id);
    List<ProductDtoRs> getAllProducts();
    ProductDtoRs createProduct(ProductCreateRq productCreateRq);
    ProductDtoRs updateProduct(ProductUpdateRq productUpdateRq);
    void deleteProduct(Long id);
}
