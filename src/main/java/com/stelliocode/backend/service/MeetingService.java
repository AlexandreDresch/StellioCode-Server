package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.MeetingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public Page<Meeting> getAllMeetings(String status, LocalDateTime scheduledAt, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledAt").descending());

        if (status != null && scheduledAt != null) {
            return meetingRepository.findByStatusAndScheduledAt(status, scheduledAt, pageable);
        } else if (status != null) {
            return meetingRepository.findByStatus(status, pageable);
        } else if (scheduledAt != null) {
            return meetingRepository.findByScheduledAt(scheduledAt, pageable);
        }

        return meetingRepository.findAll(pageable);
    }

    @Transactional
    public Meeting updateMeetingStatus(UUID meetingId, MeetingStatus newStatus) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException("Meeting not found."));

        meeting.setStatus(String.valueOf(newStatus));
        return meetingRepository.save(meeting);
    }
}
