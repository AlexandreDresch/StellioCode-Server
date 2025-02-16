package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatedMeetingResponseDTO {
    private String id;
    private String status;
    private String scheduledAt;
}
