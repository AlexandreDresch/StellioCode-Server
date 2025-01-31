package com.stelliocode.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "developer_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperProjects {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "developer_id", referencedColumnName = "id", nullable = false)
    private User developer;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @Column(name = "role_in_project")
    private String roleInProject;

    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
}

