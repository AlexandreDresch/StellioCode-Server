package com.stelliocode.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.stelliocode.backend.model.MeetingRequest;
import java.util.List;

public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
    // JpaRepository oferece métodos CRUD padrão
    List<MeetingRequest> findByStatus(MeetingRequest.Status status);
}


