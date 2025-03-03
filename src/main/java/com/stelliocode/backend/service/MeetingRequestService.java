package com.stelliocode.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stelliocode.backend.model.MeetingRequest;
import com.stelliocode.backend.repository.MeetingRequestRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service // Marca a classe como um serviço Spring
public class MeetingRequestService {

    @Autowired // Injeta o repositório automaticamente
    private MeetingRequestRepository repository;

    // Salva uma nova solicitação de reunião
    public MeetingRequest saveMeetingRequest(MeetingRequest meetingRequest) {
        if (meetingRequest == null) {
            throw new IllegalArgumentException("MeetingRequest não pode ser nulo");
        }
        meetingRequest.setStatus(MeetingRequest.Status.PENDENTE); // Define o status inicial como "Pendente"
        return repository.save(meetingRequest); // Salva no banco de dados
    }

    // Retorna todas as solicitações de reunião
    public List<MeetingRequest> getAllMeetingRequests() {
        return repository.findAll(); // Busca todas as entradas no banco de dados
    }

    // Atualiza o status de uma solicitação de reunião
    public MeetingRequest updateMeetingRequestStatus(Long id, MeetingRequest.Status status) {
        if (id == null || status == null) {
            throw new IllegalArgumentException("ID e Status não podem ser nulos");
        }
        MeetingRequest meetingRequest = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("MeetingRequest com ID " + id + " não encontrada"));
        meetingRequest.setStatus(status); // Define o novo status
        return repository.save(meetingRequest); // Salva a atualização no banco de dados
    }

    // Deleta uma solicitação de reunião
    public void deleteMeetingRequest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("MeetingRequest com ID " + id + " não encontrada");
        }
        repository.deleteById(id); // Remove a entrada do banco de dados
    }

    // Método adicional para buscar solicitações por status
    public List<MeetingRequest> getMeetingRequestsByStatus(MeetingRequest.Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        return repository.findByStatus(status);
    }
}
