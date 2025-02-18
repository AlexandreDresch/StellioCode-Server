package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID> {
    Optional<Technology> findByName(String name);

    @Query("""
        SELECT t.name FROM UserTechnology ut
        JOIN ut.technology t
        WHERE ut.user.id = :userId
    """)
    List<String> findTechnologiesByUserId(@Param("userId") UUID userId);
}
