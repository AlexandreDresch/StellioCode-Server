package com.stelliocode.backend.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProjectStatusResponseDTO {
    private UUID id;
    private String title;
    private String status;
}
