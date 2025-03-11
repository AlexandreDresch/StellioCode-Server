package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.FeaturedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeaturedProjectRepository extends JpaRepository<FeaturedProject, UUID> {
}
