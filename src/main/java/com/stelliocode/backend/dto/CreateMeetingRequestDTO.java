package com.stelliocode.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateMeetingRequestDTO {
    @NotBlank
    private String googleId;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String profilePicture;

    @NotNull
    private UUID planId;

    @NotNull
    private UUID serviceId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    @NotNull
    private LocalDateTime meetingDate;
}
