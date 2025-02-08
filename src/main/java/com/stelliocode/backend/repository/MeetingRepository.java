package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Page<Meeting> findByStatus(String status, Pageable pageable);
    Page<Meeting> findByScheduledAt(LocalDateTime scheduledAt, Pageable pageable);
    Page<Meeting> findByStatusAndScheduledAt(String status, LocalDateTime scheduledAt, Pageable pageable);
}
