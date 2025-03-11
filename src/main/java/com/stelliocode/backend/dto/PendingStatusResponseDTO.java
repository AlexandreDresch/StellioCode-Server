package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PendingStatusResponseDTO extends AuthenticationResponseBaseDTO {
    private UUID id;
    private String role;

    public PendingStatusResponseDTO(UUID id, String fullName, String role, String status) {
        super(fullName, status);
        this.id = id;
        this.role = role;
    }
}
