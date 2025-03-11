package com.stelliocode.backend.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeveloperAssignmentRemovalRequestDTO {
    private List<UUID> developerIds;
}
