package com.stelliocode.backend.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeveloperResponseDTO {
    private UUID id;
    private String fullName;
    private String status;
    private String level;
    private long activeProjects;
}
