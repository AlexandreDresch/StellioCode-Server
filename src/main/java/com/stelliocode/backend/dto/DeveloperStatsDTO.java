package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class DeveloperStatsDTO {
    private long totalDevelopers;
    private Map<String, Long> developersByStatus;
}

