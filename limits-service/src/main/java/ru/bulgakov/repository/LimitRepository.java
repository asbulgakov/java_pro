package ru.bulgakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bulgakov.model.UserLimit;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LimitRepository extends JpaRepository<UserLimit, UUID> {

    Optional<UserLimit> findByUserId(Long userId);

    @Modifying
    @Query(value = """
        UPDATE user_limits 
        SET remaining_limit = daily_limit, 
            last_reset_date = :now, 
            updated_at = :now
    """, nativeQuery = true)
    int resetAllLimits(@Param("now") LocalDateTime now);
}
