package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, UUID> {
    @Query("""
        SELECT t.name FROM Technology t
        JOIN UserTechnology ut ON ut.technology.id = t.id
        WHERE ut.user.id = :developerId
    """)
    List<String> findTechnologiesByDeveloperId(UUID developerId);

    @Modifying
    @Query("DELETE FROM UserTechnology ut WHERE ut.user.id = :developerId AND ut.technology.name IN :technologies")
    void removeTechnologies(UUID developerId, Set<String> technologies);
}
