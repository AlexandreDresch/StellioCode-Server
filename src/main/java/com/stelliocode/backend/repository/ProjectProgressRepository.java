package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.ProjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, UUID> {

    Optional<ProjectProgress> findTopByProjectIdOrderByUpdatedAtDesc(UUID projectId);

    List<ProjectProgress> findByProjectIdOrderByCreatedAtAsc(UUID projectId);
}