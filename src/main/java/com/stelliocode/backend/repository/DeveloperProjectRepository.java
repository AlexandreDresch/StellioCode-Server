package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeveloperProjectRepository extends JpaRepository<DeveloperProject, UUID> {

    @Query("""
        SELECT COUNT(dp)
        FROM DeveloperProject dp
        JOIN dp.project p
        WHERE dp.developer.id = :developerId AND p.status = 'IN_PROGRESS'
    """)
    long countActiveProjectsByDeveloperId(@Param("developerId") UUID developerId);

    @Query("SELECT COUNT(dp) FROM DeveloperProject dp WHERE dp.developer.id = :developerId")
    long countByDeveloperId(@Param("developerId") UUID developerId);

    Page<DeveloperProject> findByDeveloperId(UUID developerId, Pageable pageable);

    List<DeveloperProject> findByDeveloperId(UUID developerId);

    List<DeveloperProject> findByProjectId(UUID projectId);

    Optional<DeveloperProject> findByDeveloperIdAndProjectId(UUID developerId, UUID projectId);

    boolean existsByDeveloperAndProject(User developer, Project project);

    boolean existsByDeveloperIdAndProjectId(UUID developerId, UUID projectId);
}

