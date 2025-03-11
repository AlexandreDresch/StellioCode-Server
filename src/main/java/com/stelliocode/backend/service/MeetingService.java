package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.MeetingResponseDTO;
import com.stelliocode.backend.dto.UpdatedMeetingResponseDTO;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.MeetingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Meeting createInitialMeeting(String googleId, String name, String email, String phone, String profilePicture,
                                        UUID planId, UUID serviceId, String title, String description, Double price,
                                        LocalDateTime meetingDate) {
        Client client = clientService.createClient(googleId, name, email, phone, profilePicture);
        Project project = projectService.createProject(title, description, price, client, planId, serviceId);

        Meeting meeting = new Meeting();
        meeting.setClient(client);
        meeting.setProject(project);
        meeting.setScheduledAt(meetingDate.toString());
        meeting.setStatus("pending");
        return meetingRepository.save(meeting);
    }

    public Page<MeetingResponseDTO> getAllMeetings(String status, LocalDateTime scheduledAt, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledAt").descending());
        Page<Meeting> meetingsPage;

        if (status != null && scheduledAt != null) {
            meetingsPage = meetingRepository.findByStatusAndScheduledAt(status, scheduledAt, pageable);
        } else if (status != null) {
            meetingsPage = meetingRepository.findByStatus(status, pageable);
        } else if (scheduledAt != null) {
            meetingsPage = meetingRepository.findByScheduledAt(scheduledAt, pageable);
        } else {
            meetingsPage = meetingRepository.findAll(pageable);
        }

        List<MeetingResponseDTO> meetingsDTO = meetingsPage.getContent()
                .stream()
                .map(MeetingResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(meetingsDTO, pageable, meetingsPage.getTotalElements());
    }

    @Transactional
    public UpdatedMeetingResponseDTO updateMeetingStatus(UUID meetingId, MeetingStatus newStatus) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found."));

        meeting.setStatus(String.valueOf(newStatus));
        meetingRepository.save(meeting);

        return new UpdatedMeetingResponseDTO(
                meeting.getId().toString(),
                meeting.getStatus(),
                meeting.getScheduledAt()
        );
    }

    @Transactional
    public UpdatedMeetingResponseDTO updateMeetingDate(UUID meetingId, String date) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found."));

        meeting.setScheduledAt(date);
        meetingRepository.save(meeting);

        return new UpdatedMeetingResponseDTO(
                meeting.getId().toString(),
                meeting.getStatus(),
                meeting.getScheduledAt()
        );
    }
}
