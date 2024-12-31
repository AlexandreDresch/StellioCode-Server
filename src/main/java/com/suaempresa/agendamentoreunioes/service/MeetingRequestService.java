package com.suaempresa.agendamentoreunioes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.suaempresa.agendamentoreunioes.model.MeetingRequest;
import com.suaempresa.agendamentoreunioes.repository.MeetingRequestRepository;

import java.util.List;

@Service // Marca a classe como um serviço Spring
public class MeetingRequestService {

    @Autowired // Injeta o repositório automaticamente
    private MeetingRequestRepository repository;

    // Salva uma nova solicitação de reunião
    public MeetingRequest saveMeetingRequest(MeetingRequest meetingRequest) {
        meetingRequest.setStatus(MeetingRequest.Status.PENDENTE); // Define o status inicial como "Pendente"
        return repository.save(meetingRequest); // Salva no banco de dados
    }

    // Retorna todas as solicitações de reunião
    public List<MeetingRequest> getAllMeetingRequests() {
        return repository.findAll(); // Busca todas as entradas no banco de dados
    }

    // Atualiza o status de uma solicitação de reunião
    public MeetingRequest updateMeetingRequestStatus(Long id, MeetingRequest.Status status) {
        MeetingRequest meetingRequest = repository.findById(id).orElseThrow(); // Encontra a solicitação pelo ID
        meetingRequest.setStatus(status); // Define o novo status
        return repository.save(meetingRequest); // Salva a atualização no banco de dados
    }

    // Deleta uma solicitação de reunião
    public void deleteMeetingRequest(Long id) {
        repository.deleteById(id); // Remove a entrada do banco de dados
    }
}
