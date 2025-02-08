package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("SELECT COUNT(c) FROM Client c WHERE FUNCTION('DATE', c.createdAt) >= :startDate")
    int getNewClientsByMonth(LocalDate startDate);

    Optional<Client> findByGoogleId(String googleId);
    Optional<Client> findByEmail(String email);
}