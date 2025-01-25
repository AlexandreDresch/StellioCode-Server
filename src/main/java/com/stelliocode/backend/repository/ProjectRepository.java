package com.stelliocode.backend.repository;
import com.stelliocode.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query("SELECT COUNT(p) FROM Project p")
    Long countAllProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'completed'")
    Long countCompletedProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'pending'")
    Long countPendingProjects();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    Integer countNewProjects(LocalDateTime startDate, LocalDateTime endDate);
}