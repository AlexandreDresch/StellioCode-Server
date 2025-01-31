package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.DeveloperProjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeveloperProjectsRepository extends JpaRepository<DeveloperProjects, UUID> {

    List<DeveloperProjects> findByProjectId(UUID projectId);
}

