package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    Optional<User> findById(UUID developerId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'developer'")
    long countTotalDevelopers();

    @Query("SELECT u.status, COUNT(u) FROM User u WHERE u.role = 'developer' GROUP BY u.status")
    List<Object[]> countDevelopersByStatus();

    List<User> findByRoleAndStatus(String role, String status);

    void deleteById(UUID id);
}