package com.stelliocode.backend.entity;

public enum ProjectStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}