package ru.bulgakov.spring.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bulgakov.spring.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(value = "product-with-user", type = EntityGraph.EntityGraphType.LOAD)
    List<Product> findByUserId(Long userId);

    boolean existsByAccountNumber(String accountNumber);

    @EntityGraph(value = "product-with-user", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findById(@Nonnull Long id);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    void deleteById(@Nonnull Long id);
}
