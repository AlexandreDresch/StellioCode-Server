package com.stelliocode.backend.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InitialMeetingResponseDTO {
    private UUID clientId;
    private String clientName;
    private String projectTitle;
    private String projectStatus;
    private String meetingStatus;
    private String scheduledAt;
}
