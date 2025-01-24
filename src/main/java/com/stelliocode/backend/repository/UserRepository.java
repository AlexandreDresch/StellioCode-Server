package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u")
    Long countAllUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    Integer countNewUsers(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
    SELECT u 
    FROM User u 
    WHERE u.role = :role 
      AND (:name IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:status IS NULL OR u.status = :status)
""")
    Page<User> findAllByRoleAndFilters(
            @Param("role") String role,
            @Param("name") String name,
            @Param("status") String status,
            Pageable pageable
    );
}