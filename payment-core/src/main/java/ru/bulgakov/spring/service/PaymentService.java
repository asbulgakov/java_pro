package ru.bulgakov.spring.service;

import ru.bulgakov.spring.dto.payment.PaymentDtoRq;
import ru.bulgakov.spring.dto.payment.PaymentDtoRs;
import ru.bulgakov.spring.dto.product.UserProductsResponseRs;

public interface PaymentService {
    UserProductsResponseRs getUserProducts(Long userId);

    PaymentDtoRs processPayment(PaymentDtoRq paymentRequest);
}
