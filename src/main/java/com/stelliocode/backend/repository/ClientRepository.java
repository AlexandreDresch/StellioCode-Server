package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByGoogleId(String googleId);
    Optional<Client> findByEmail(String email);
}