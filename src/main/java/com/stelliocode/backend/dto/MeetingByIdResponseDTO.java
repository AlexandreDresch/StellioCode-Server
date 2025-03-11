package com.stelliocode.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MeetingByIdResponseDTO {
    private UUID id;
    private String status;
    private UUID clientId;
    private String clientName;
    private UUID projectId;
    private String projectName;
    private String projectDescription;
    private String scheduledAt;
    private List<String> participants;
}
