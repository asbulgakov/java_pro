package ru.bulgakov.spring.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.bulgakov.spring.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteById(@Nonnull @Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);
}
