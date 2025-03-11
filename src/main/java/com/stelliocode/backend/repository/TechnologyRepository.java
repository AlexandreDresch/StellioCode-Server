package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TechnologyRepository extends JpaRepository<Technology, UUID> {
    Optional<Technology> findByName(String name);
}
