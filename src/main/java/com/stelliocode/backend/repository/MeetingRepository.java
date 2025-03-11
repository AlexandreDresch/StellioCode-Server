package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Page<Meeting> findByStatus(String status, Pageable pageable);
    Page<Meeting> findByScheduledAt(LocalDateTime scheduledAt, Pageable pageable);
    Page<Meeting> findByStatusAndScheduledAt(String status, LocalDateTime scheduledAt, Pageable pageable);
    Page<Meeting> findByProjectIdIn(List<UUID> projectIds, Pageable pageable);

    @Query("SELECT m FROM Meeting m WHERE m.project.id = :projectId ORDER BY m.scheduledAt ASC")
    List<Meeting> findByProjectIdOrderByScheduledAtAsc(@Param("projectId") UUID projectId);
}
