package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.DeveloperProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeveloperProjectRepository extends JpaRepository<DeveloperProject, UUID> {

    @Query("""
        SELECT COUNT(dp) 
        FROM DeveloperProject dp 
        JOIN dp.project p 
        WHERE dp.developer.id = :developerId AND p.status = 'in_progress'
    """)
    long countActiveProjectsByDeveloperId(@Param("developerId") UUID developerId);

    Optional<DeveloperProject> findByDeveloperIdAndProjectId(UUID developerId, UUID projectId);
}

