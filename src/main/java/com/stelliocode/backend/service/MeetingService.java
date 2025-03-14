package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.MeetingByIdResponseDTO;
import com.stelliocode.backend.dto.MeetingResponseDTO;
import com.stelliocode.backend.dto.UpdatedMeetingResponseDTO;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final PaymentService paymentService;
    private final MeetingRepository meetingRepository;
    private final DeveloperProjectRepository developerProjectRepository;
    private final ServiceRepository serviceRepository;
    private final PlanRepository planRepository;


    @Transactional
    public Meeting createInitialMeeting(String googleId, String name, String email, String phone, String profilePicture,
                                        UUID planId, UUID serviceId, String title, String description, Double price,
                                        LocalDateTime meetingDate) {
        Client client = clientService.createClient(googleId, name, email, phone, profilePicture);

        com.stelliocode.backend.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));

        Project project = projectService.createProject(title, description, price, client, plan.getId(), service.getId());

        BigDecimal totalAmount = BigDecimal.valueOf(price).add(service.getPrice());

        paymentService.createInitialPayment(project, totalAmount);

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

    public Page<MeetingResponseDTO> getMeetingsByDeveloper(UUID developerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledAt").descending());

        List<UUID> projectIds = developerProjectRepository.findByDeveloperId(developerId)
                .stream()
                .map(dp -> dp.getProject().getId())
                .toList();

        Page<Meeting> meetingsPage = meetingRepository.findByProjectIdIn(projectIds, pageable);

        List<MeetingResponseDTO> meetingsDTO = meetingsPage.getContent()
                .stream()
                .map(MeetingResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(meetingsDTO, pageable, meetingsPage.getTotalElements());
    }

    public List<MeetingByIdResponseDTO> getAllMeetingsByProjectId(UUID projectId) {
        List<Meeting> meetings = meetingRepository.findByProjectIdOrderByScheduledAtAsc(projectId);

        return meetings.stream()
                .map(meeting -> {
                    List<String> participants = developerProjectRepository.findByProjectId(projectId)
                            .stream()
                            .map(developerProject -> developerProject.getDeveloper().getFullName())
                            .collect(Collectors.toList());

                    // Adiciona o cliente como participante
                    participants.add(meeting.getClient().getName());

                    return MeetingByIdResponseDTO.builder()
                            .id(meeting.getId())
                            .status(meeting.getStatus())
                            .clientId(meeting.getClient().getId())
                            .clientName(meeting.getClient().getName())
                            .projectId(meeting.getProject().getId())
                            .projectName(meeting.getProject().getTitle())
                            .projectDescription(meeting.getProject().getDescription())
                            .scheduledAt(meeting.getScheduledAt())
                            .participants(participants)
                            .build();
                })
                .collect(Collectors.toList());
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
