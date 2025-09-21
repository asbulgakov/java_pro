package ru.bulgakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bulgakov.model.UserLimit;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LimitRepository extends JpaRepository<UserLimit, UUID> {
    Optional<UserLimit> findByUserId(Long userId);
}
