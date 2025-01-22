package com.stelliocode.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AuthenticationResponseBaseDTO {
    private String fullName;
    private String status;

    public AuthenticationResponseBaseDTO(String fullName, String status) {
        this.fullName = fullName;
        this.status = status;
    }
}
