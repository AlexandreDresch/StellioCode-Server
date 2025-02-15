package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeetingResponseDTO {
    private String id;
    private String status;
    private String clientId;
    private String clientName;
    private String projectId;
    private String projectName;
    private String projectDescription;
    private String scheduledAt;

    public static MeetingResponseDTO fromEntity(Meeting meeting) {
        return new MeetingResponseDTO(
                meeting.getId().toString(),
                meeting.getStatus(),
                meeting.getClient().getId().toString(),
                meeting.getClient().getName(),
                meeting.getProject().getId().toString(),
                meeting.getProject().getTitle(),
                meeting.getProject().getDescription(),
                meeting.getScheduledAt().toString()
        );
    }
}
