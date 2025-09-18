package ru.bulgakov.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bulgakov.spring.dto.payment.PaymentDtoRq;
import ru.bulgakov.spring.dto.payment.PaymentDtoRs;
import ru.bulgakov.spring.dto.product.UserProductsResponseRs;
import ru.bulgakov.spring.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/users/{userId}/products")
    public UserProductsResponseRs getUserProducts(@PathVariable Long userId) {
        return paymentService.getUserProducts(userId);
    }

    @PostMapping
    public PaymentDtoRs processPayment(@RequestBody PaymentDtoRq paymentDtoRq) {
        return paymentService.processPayment(paymentDtoRq);
    }
}
