package com.stelliocode.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "technologies")
@Data
@NoArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    public Technology(String name) {
        this.name = name;
    }
}
