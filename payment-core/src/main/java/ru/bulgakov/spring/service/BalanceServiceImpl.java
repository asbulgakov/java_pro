package ru.bulgakov.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bulgakov.spring.client.ProductClient;
import ru.bulgakov.spring.dto.product.ProductDtoRs;
import ru.bulgakov.spring.dto.product.ProductUpdateRq;
import ru.bulgakov.spring.exception.BalanceUpdateException;
import ru.bulgakov.spring.exception.InsufficientFundsException;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final ProductClient productClient;

    @Override
    @Transactional
    public void updateProductBalance(ProductDtoRs product, BigDecimal amountToDeduct) {
        validateBalanceUpdate(product, amountToDeduct);

        BigDecimal newBalance = product.balance().subtract(amountToDeduct);

        ProductUpdateRq updateRequest = createBalanceUpdateRequest(product, newBalance);

        try {
            ProductDtoRs updatedProduct = productClient.updateBalanceUserProduct(updateRequest);
            log.info("Баланс успешно обновлен. Продукт: {}, списано: {}, новый баланс: {}",
                    product.id(), amountToDeduct, updatedProduct.balance());

        } catch (RuntimeException e) {
            log.error("Ошибка при обновлении баланса продукта {}: {}", product.id(), e.getMessage());
            throw new BalanceUpdateException("Не удалось обновить баланс продукта", e);
        }
    }

    @Override
    public BigDecimal calculateNewBalance(ProductDtoRs product, BigDecimal amountToDeduct) {
        validateBalanceUpdate(product, amountToDeduct);
        return product.balance().subtract(amountToDeduct);
    }

    private void validateBalanceUpdate(ProductDtoRs product, BigDecimal amountToDeduct) {
        if (product == null) {
            throw new BalanceUpdateException("Продукт не может быть null");
        }

        if (amountToDeduct == null || amountToDeduct.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BalanceUpdateException("Сумма для списания должна быть положительной");
        }

        if (product.balance().compareTo(amountToDeduct) < 0) {
            throw new InsufficientFundsException(
                    String.format("Недостаточно средств. Доступно: %.2f, требуется: %.2f",
                            product.balance(), amountToDeduct)
            );
        }
    }

    private ProductUpdateRq createBalanceUpdateRequest(ProductDtoRs product, BigDecimal newBalance) {
        return new ProductUpdateRq(
                product.id(),
                product.accountNumber(),
                newBalance,
                product.productType()
        );
    }
}
