package com.stelliocode.backend.repository;
import com.stelliocode.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT COUNT(p) FROM Project p")
    Long countAllProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'completed'")
    Long countCompletedProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'pending'")
    Long countPendingProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    Integer countNewProjects(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Project p JOIN DeveloperProject dp ON p.id = dp.project.id WHERE dp.developer.id = :developerId")
    Page<Project> findByDeveloperId(@Param("developerId") UUID developerId, Pageable pageable);

    @Query("SELECT p FROM Project p JOIN DeveloperProject dp ON p.id = dp.project.id WHERE dp.developer.id = :developerId AND p.status = :status")
    Page<Project> findByDeveloperIdAndStatus(@Param("developerId") UUID developerId, @Param("status") String status, Pageable pageable);
}