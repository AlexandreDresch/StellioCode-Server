package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {
    private UUID id;
    private String fullName;
    private String status;
}
