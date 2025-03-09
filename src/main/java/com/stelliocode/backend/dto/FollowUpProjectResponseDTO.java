package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.Payment;
import com.stelliocode.backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import static com.stelliocode.backend.util.MonetaryUtils.formatToBRL;

@Data
@AllArgsConstructor
@Builder
public class FollowUpProjectResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private String price;
    private UUID paymentId;
    private String paymentStatus;
    private String paymentAmount;
    private UUID planId;
    private String planName;
    private String planPrice;
    private String planDescription;
    private String planPeriod;
    private UUID serviceId;
    private String serviceName;
    private String serviceDescription;
    private String servicePrice;
    private String clientName;

    public static FollowUpProjectResponseDTO fromProject(Project project, Payment payment) {
        return FollowUpProjectResponseDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .status(String.valueOf(project.getStatus()))
                .price(formatToBRL(project.getPrice()))
                .paymentId(UUID.fromString(payment.getId()))
                .paymentStatus(String.valueOf(payment.getPaymentStatus()))
                .paymentAmount(formatToBRL(payment.getAmount()))
                .planId(project.getPlan().getId())
                .planName(project.getPlan().getName())
                .planPrice(formatToBRL(project.getPlan().getPrice()))
                .planDescription(project.getPlan().getDescription())
                .planPeriod(project.getPlan().getPeriod())
                .serviceId(project.getService().getId())
                .serviceName(project.getService().getTitle())
                .serviceDescription(project.getService().getDescription())
                .servicePrice(formatToBRL(project.getService().getPrice()))
                .clientName(project.getClient().getName())
                .build();
    }
}
