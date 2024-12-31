package com.suaempresa.agendamentoreunioes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suaempresa.agendamentoreunioes.model.MeetingRequest;

public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
    // JpaRepository oferece métodos CRUD padrão
}
