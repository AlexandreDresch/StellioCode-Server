package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AuthenticationResponseDTO extends AuthenticationResponseBaseDTO {
    private String token;
    private UUID id;
    private String role;

    public AuthenticationResponseDTO(String token, UUID id, String fullName, String role, String status) {
        super(fullName, status);
        this.token = token;
        this.id = id;
        this.role = role;
    }
}
