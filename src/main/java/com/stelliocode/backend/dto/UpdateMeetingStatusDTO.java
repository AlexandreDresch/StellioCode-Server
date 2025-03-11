package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.MeetingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateMeetingStatusDTO(
        @NotNull(message = "Status cannot be null.")
        MeetingStatus status
) {}