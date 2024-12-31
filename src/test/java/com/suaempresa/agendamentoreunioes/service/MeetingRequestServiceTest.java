package com.suaempresa.agendamentoreunioes.service;

import com.suaempresa.agendamentoreunioes.model.MeetingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MeetingRequestServiceTest {

    private MeetingRequestService service;

    @BeforeEach
    void setUp() {
        service = new MeetingRequestService();
    }

    @Test
    void testSaveMeetingRequest() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setClientId("123");
        meetingRequest.setClientName("John Doe");
        meetingRequest.setClientEmail("john.doe@example.com");
        meetingRequest.setClientPhone("1234567890");
        meetingRequest.setMeetingDateTime(LocalDateTime.now().plusDays(1));
        meetingRequest.setNotes("Discuss project details.");

        MeetingRequest savedMeeting = service.saveMeetingRequest(meetingRequest);

        assertNotNull(savedMeeting.getId());
        assertEquals(MeetingRequest.Status.PENDENTE, savedMeeting.getStatus());
        assertEquals("John Doe", savedMeeting.getClientName());
    }

    @Test
    void testGetAllMeetingRequests() {
        MeetingRequest meetingRequest1 = new MeetingRequest();
        meetingRequest1.setClientId("123");
        meetingRequest1.setClientName("John Doe");
        meetingRequest1.setClientEmail("john.doe@example.com");
        meetingRequest1.setClientPhone("1234567890");
        meetingRequest1.setMeetingDateTime(LocalDateTime.now().plusDays(1));
        meetingRequest1.setNotes("Discuss project details.");

        MeetingRequest meetingRequest2 = new MeetingRequest();
        meetingRequest2.setClientId("124");
        meetingRequest2.setClientName("Jane Doe");
        meetingRequest2.setClientEmail("jane.doe@example.com");
        meetingRequest2.setClientPhone("0987654321");
        meetingRequest2.setMeetingDateTime(LocalDateTime.now().plusDays(2));
        meetingRequest2.setNotes("Review budget.");

        service.saveMeetingRequest(meetingRequest1);
        service.saveMeetingRequest(meetingRequest2);

        List<MeetingRequest> allMeetingRequests = service.getAllMeetingRequests();

        assertEquals(2, allMeetingRequests.size());
    }

    @Test
    void testUpdateMeetingRequestStatus() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setClientId("123");
        meetingRequest.setClientName("John Doe");
        meetingRequest.setClientEmail("john.doe@example.com");
        meetingRequest.setClientPhone("1234567890");
        meetingRequest.setMeetingDateTime(LocalDateTime.now().plusDays(1));
        meetingRequest.setNotes("Discuss project details.");

        MeetingRequest savedMeeting = service.saveMeetingRequest(meetingRequest);
        Long meetingId = savedMeeting.getId();

        Optional<MeetingRequest> updatedMeeting = service.updateMeetingRequestStatus(meetingId, MeetingRequest.Status.ACEITA);

        assertTrue(updatedMeeting.isPresent());
        assertEquals(MeetingRequest.Status.ACEITA, updatedMeeting.get().getStatus());
    }

    @Test
    void testDeleteMeetingRequest() {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setClientId("123");
        meetingRequest.setClientName("John Doe");
        meetingRequest.setClientEmail("john.doe@example.com");
        meetingRequest.setClientPhone("1234567890");
        meetingRequest.setMeetingDateTime(LocalDateTime.now().plusDays(1));
        meetingRequest.setNotes("Discuss project details.");

        MeetingRequest savedMeeting = service.saveMeetingRequest(meetingRequest);
        Long meetingId = savedMeeting.getId();

        boolean isDeleted = service.deleteMeetingRequest(meetingId);

        assertTrue(isDeleted);
        assertTrue(service.getAllMeetingRequests().isEmpty());
    }
}
