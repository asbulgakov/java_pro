package ru.bulgakov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bulgakov.spring.dto.product.rq.ProductCreateRq;
import ru.bulgakov.spring.dto.product.rq.ProductUpdateRq;
import ru.bulgakov.spring.dto.product.rs.ProductDtoRs;
import ru.bulgakov.spring.exception.ProductAlreadyExistsException;
import ru.bulgakov.spring.exception.ProductDeletionException;
import ru.bulgakov.spring.exception.ProductNotFoundException;
import ru.bulgakov.spring.exception.UserNotFoundException;
import ru.bulgakov.spring.mapper.ProductMapper;
import ru.bulgakov.spring.model.Product;
import ru.bulgakov.spring.model.User;
import ru.bulgakov.spring.repository.ProductRepository;
import ru.bulgakov.spring.repository.UserRepository;
import ru.bulgakov.spring.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDtoRs> getProductsByUserId(Long userId) {
        log.info("Getting products for user id: {}", userId);
        validateUserId(userId);
        return productRepository.findByUserId(userId).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDtoRs getProductById(Long id) {
        log.info("Getting product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDtoRs> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDtoRs createProduct(ProductCreateRq dto) {
        log.info("Creating product for user id: {}", dto.userId());
        validateProductCreateRq(dto);

        if (productRepository.existsByAccountNumber(dto.accountNumber())) {
            throw new ProductAlreadyExistsException("Product with account number '" + dto.accountNumber() + "' already exists");
        }

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.userId()));

        Product product = productMapper.toEntity(dto);
        product.setUser(user);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDtoRs updateProduct(ProductUpdateRq dto) {
        log.info("Updating product with id: {}", dto.id());
        validateProductUpdateRq(dto);

        Product product = productRepository.findById(dto.id())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + dto.id()));

        if (!product.getAccountNumber().equals(dto.accountNumber()) &&
                productRepository.existsByAccountNumber(dto.accountNumber())) {
            throw new ProductAlreadyExistsException("Product with account number '" + dto.accountNumber() + "' already exists");
        }

        product.setAccountNumber(dto.accountNumber());
        product.setBalance(dto.balance());
        product.setProductType(dto.productType());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        validateProductId(id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new ProductNotFoundException("Product not found with id: " + id);
                });

        try {
            productRepository.delete(product);
            log.info("Product deleted successfully with id: {}", id);
        } catch (DataAccessException e) {
            log.error("Error deleting product with id: {}", id, e);
            throw new ProductDeletionException("Failed to delete product with id: " + id, e);
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
    }

    private void validateProductCreateRq(ProductCreateRq dto) {
        if (dto.accountNumber() == null || dto.accountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (dto.productType() == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }
        validateUserId(dto.userId());

        if (dto.balance() != null && dto.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    private void validateProductUpdateRq(ProductUpdateRq dto) {
        validateProductId(dto.id());
        if (dto.accountNumber() == null || dto.accountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (dto.productType() == null) {
            throw new IllegalArgumentException("Product type cannot be null");
        }
        if (dto.balance() != null && dto.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
}
