package com.stelliocode.backend.dto;

public record SummaryDTO(SummaryMetricDTO totalRevenue, SummaryMetricDTO completedProjects,
                         SummaryMetricDTO newProjects, SummaryMetricDTO newClients) {
}
