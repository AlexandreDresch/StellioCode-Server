package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponseDTO {
    private String message;
    private boolean success;
}
