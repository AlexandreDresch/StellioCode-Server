package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, UUID> {
}
