package com.stelliocode.backend.service;

import com.stelliocode.backend.model.MeetingRequest;
import com.stelliocode.backend.repository.MeetingRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MeetingRequestServiceTest {

    @Mock
    private MeetingRequestRepository repository;

    @InjectMocks
    private MeetingRequestService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    // Teste para salvar uma MeetingRequest com sucesso
    @Test
    public void testSaveMeetingRequest_Success() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setId(1L);
        meetingRequest.setTitle("Reunião de Planejamento");

        when(repository.save(any(MeetingRequest.class))).thenReturn(meetingRequest);

        MeetingRequest savedRequest = service.saveMeetingRequest(meetingRequest);

        assertNotNull(savedRequest);
        assertEquals(MeetingRequest.Status.PENDENTE, savedRequest.getStatus()); // Verifica o status padrão
        assertEquals("Reunião de Planejamento", savedRequest.getTitle());
        verify(repository, times(1)).save(meetingRequest); // Verifica se o repositório foi chamado
    }

    // Teste para salvar uma MeetingRequest nula
    @Test
    public void testSaveMeetingRequest_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.saveMeetingRequest(null);
        });
        verify(repository, never()).save(any()); // Verifica se o repositório NÃO foi chamado
    }

    // Teste para buscar todas as MeetingRequests
    @Test
    public void testGetAllMeetingRequests() {
        MeetingRequest meetingRequest1 = new MeetingRequest();
        meetingRequest1.setId(1L);
        meetingRequest1.setTitle("Reunião 1");

        MeetingRequest meetingRequest2 = new MeetingRequest();
        meetingRequest2.setId(2L);
        meetingRequest2.setTitle("Reunião 2");

        when(repository.findAll()).thenReturn(List.of(meetingRequest1, meetingRequest2));

        List<MeetingRequest> requests = service.getAllMeetingRequests();

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals("Reunião 1", requests.get(0).getTitle());
        assertEquals("Reunião 2", requests.get(1).getTitle());
        verify(repository, times(1)).findAll(); // Verifica se o repositório foi chamado
    }

    // Teste para atualizar o status de uma MeetingRequest com sucesso
    @Test
    public void testUpdateMeetingRequestStatus_Success() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setId(1L);
        meetingRequest.setStatus(MeetingRequest.Status.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(meetingRequest));
        when(repository.save(any(MeetingRequest.class))).thenReturn(meetingRequest);

        MeetingRequest updatedRequest = service.updateMeetingRequestStatus(1L, MeetingRequest.Status.ACEITA);

        assertNotNull(updatedRequest);
        assertEquals(MeetingRequest.Status.ACEITA, updatedRequest.getStatus());
        verify(repository, times(1)).findById(1L); // Verifica se o repositório foi chamado
        verify(repository, times(1)).save(meetingRequest); // Verifica se o repositório foi chamado
    }

    // Teste para atualizar o status de uma MeetingRequest com ID inválido
    @Test
    public void testUpdateMeetingRequestStatus_InvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            service.updateMeetingRequestStatus(1L, MeetingRequest.Status.ACEITA);
        });
        verify(repository, times(1)).findById(1L); // Verifica se o repositório foi chamado
        verify(repository, never()).save(any()); // Verifica se o repositório NÃO foi chamado
    }

    // Teste para deletar uma MeetingRequest com sucesso
    @Test
    public void testDeleteMeetingRequest_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> {
            service.deleteMeetingRequest(1L);
        });

        verify(repository, times(1)).existsById(1L); // Verifica se o repositório foi chamado
        verify(repository, times(1)).deleteById(1L); // Verifica se o repositório foi chamado
    }

    // Teste para deletar uma MeetingRequest com ID inválido
    @Test
    public void testDeleteMeetingRequest_InvalidId() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            service.deleteMeetingRequest(1L);
        });
        verify(repository, times(1)).existsById(1L); // Verifica se o repositório foi chamado
        verify(repository, never()).deleteById(any()); // Verifica se o repositório NÃO foi chamado
    }

    // Teste para buscar MeetingRequests por status
    @Test
    public void testGetMeetingRequestsByStatus() {
        MeetingRequest meetingRequest1 = new MeetingRequest();
        meetingRequest1.setId(1L);
        meetingRequest1.setStatus(MeetingRequest.Status.ACEITA);

        MeetingRequest meetingRequest2 = new MeetingRequest();
        meetingRequest2.setId(2L);
        meetingRequest2.setStatus(MeetingRequest.Status.ACEITA);

        when(repository.findByStatus(MeetingRequest.Status.ACEITA))
                .thenReturn(List.of(meetingRequest1, meetingRequest2));

        List<MeetingRequest> requests = service.getMeetingRequestsByStatus(MeetingRequest.Status.ACEITA);

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(MeetingRequest.Status.ACEITA, requests.get(0).getStatus());
        assertEquals(MeetingRequest.Status.ACEITA, requests.get(1).getStatus());
        verify(repository, times(1)).findByStatus(MeetingRequest.Status.ACEITA); // Verifica se o repositório foi chamado
    }

    // Teste para buscar MeetingRequests por status nulo
    @Test
    public void testGetMeetingRequestsByStatus_NullStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.getMeetingRequestsByStatus(null);
        });
        verify(repository, never()).findByStatus(any()); // Verifica se o repositório NÃO foi chamado
    }

    // Teste para atualizar o status de uma MeetingRequest com status nulo
    @Test
    public void testUpdateMeetingRequestStatus_NullStatus() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setId(1L);
        meetingRequest.setStatus(MeetingRequest.Status.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(meetingRequest));

        assertThrows(IllegalArgumentException.class, () -> {
            service.updateMeetingRequestStatus(1L, null);
        });
        verify(repository, never()).save(any()); // Verifica se o repositório NÃO foi chamado
    }

    // Teste para atualizar o status de uma MeetingRequest com ID nulo
    @Test
    public void testUpdateMeetingRequestStatus_NullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateMeetingRequestStatus(null, MeetingRequest.Status.ACEITA);
        });
        verify(repository, never()).findById(any()); // Verifica se o repositório NÃO foi chamado
    }
}