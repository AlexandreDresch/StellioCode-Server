package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTechnologyRepository extends JpaRepository<UserTechnology, UUID> {
}
