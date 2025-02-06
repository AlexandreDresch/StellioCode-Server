package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.CreateMeetingRequestDTO;
import com.stelliocode.backend.dto.InitialMeetingResponseDTO;
import com.stelliocode.backend.entity.Meeting;
import com.stelliocode.backend.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/initial")
    public ResponseEntity<InitialMeetingResponseDTO> createInitialMeeting(@RequestBody CreateMeetingRequestDTO request) {
        Meeting meeting = meetingService.createInitialMeeting(
                request.getGoogleId(),
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getProfilePicture(),
                request.getPlanId(),
                request.getServiceId(),
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getMeetingDate()
        );

        InitialMeetingResponseDTO response = new InitialMeetingResponseDTO(
                meeting.getClient().getId(),
                meeting.getClient().getName(),
                meeting.getProject().getTitle(),
                meeting.getProject().getStatus().toString(),
                meeting.getStatus(),
                meeting.getScheduledAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
