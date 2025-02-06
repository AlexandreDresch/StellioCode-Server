package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.MeetingRepository;

import lombok.RequiredArgsConstructor;
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
}
