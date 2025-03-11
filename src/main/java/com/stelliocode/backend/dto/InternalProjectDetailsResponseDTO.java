package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InternalProjectDetailsResponseDTO {
    private String id;
    private String title;
    private String description;
    private String status;
    private BigDecimal price;
    private String planName;
    private String serviceName;
    private String clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String clientProfilePicture;
    private List<DeveloperDTO> developers;
}
