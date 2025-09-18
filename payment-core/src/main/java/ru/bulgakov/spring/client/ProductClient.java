package ru.bulgakov.spring.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.bulgakov.spring.dto.product.ProductDtoRs;
import ru.bulgakov.spring.dto.product.ProductUpdateRq;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {
    private final RestTemplate restTemplate;

    @Value("${services.product-service.url}")
    private String productServiceUrl;

    public List<ProductDtoRs> getUserProducts(Long userId) {
        String url = productServiceUrl + "/products/users/" + userId;
        log.debug("Requesting user products from: {}", url);

        ProductDtoRs[] productsArray = restTemplate.getForObject(url, ProductDtoRs[].class);
        return productsArray != null ? Arrays.asList(productsArray) : Collections.emptyList();
    }

    public ProductDtoRs updateBalanceUserProduct(ProductUpdateRq productUpdateRq) {
        String url = productServiceUrl + "/products";
        return restTemplate.patchForObject(url, productUpdateRq, ProductDtoRs.class);
    }

    public ProductDtoRs getProductById(Long productId) {
        String url = productServiceUrl + "/products/" + productId;
        return restTemplate.getForObject(url, ProductDtoRs.class);
    }
}
