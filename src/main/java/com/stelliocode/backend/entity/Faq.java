package com.stelliocode.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Faq {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    public void associateWithPlan(Plan plan) {
        this.entityType = "Plan";
        this.entityId = plan.getId();
    }

    public void associateWithService(Service service) {
        this.entityType = "Service";
        this.entityId = service.getId();
    }
}