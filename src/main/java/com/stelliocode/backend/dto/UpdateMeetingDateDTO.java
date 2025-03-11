package com.stelliocode.backend.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateMeetingDateDTO(
        @NotNull(message = "Date cannot be null.")
        String date
) {}