package com.suaempresa.agendamentoreunioes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import com.suaempresa.agendamentoreunioes.model.MeetingRequest;
import com.suaempresa.agendamentoreunioes.service.MeetingRequestService;
import java.util.List;

@RestController // Marca a classe como um controlador REST do Spring
@RequestMapping("/api/meetings") // Define a URL base para todos os endpoints nesta classe
public class MeetingRequestController {

    @Autowired // Injeta o serviço automaticamente
    private MeetingRequestService service;

    // Endpoint para criar uma nova solicitação de reunião
    @PostMapping
    public ResponseEntity<MeetingRequest> createMeetingRequest(@Valid @RequestBody MeetingRequest meetingRequest) {
        MeetingRequest savedMeeting = service.saveMeetingRequest(meetingRequest); // Salva a nova solicitação
        // Aqui você pode adicionar a lógica para enviar confirmação por email
        return ResponseEntity.ok(savedMeeting); // Retorna a solicitação salva
    }

    // Endpoint para obter todas as solicitações de reunião
    @GetMapping
    public ResponseEntity<List<MeetingRequest>> getAllMeetingRequests() {
        List<MeetingRequest> meetingRequests = service.getAllMeetingRequests(); // Busca todas as solicitações
        return ResponseEntity.ok(meetingRequests); // Retorna a lista de solicitações
    }

    // Endpoint para atualizar o status de uma solicitação de reunião
    @PatchMapping("/{id}/status")
    public ResponseEntity<MeetingRequest> updateMeetingRequestStatus(@PathVariable Long id, @RequestBody String status) {
        MeetingRequest.Status newStatus = MeetingRequest.Status.valueOf(status.toUpperCase()); // Converte a string para enum
        MeetingRequest updatedMeeting = service.updateMeetingRequestStatus(id, newStatus); // Atualiza o status
        // Aqui você pode adicionar a lógica para enviar notificação por email
        return ResponseEntity.ok(updatedMeeting); // Retorna a solicitação atualizada
    }

    // Endpoint para deletar uma solicitação de reunião
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRequest(@PathVariable Long id) {
        service.deleteMeetingRequest(id); // Deleta a solicitação
        return ResponseEntity.noContent().build(); // Retorna uma resposta sem conteúdo
    }
}
