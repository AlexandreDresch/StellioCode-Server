package com.stelliocode.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "developer_projects")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private User developer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "role_in_project", length = 50)
    private String roleInProject;

    @Column(name = "assigned_at", nullable = false)
    private java.time.LocalDateTime assignedAt;
}

