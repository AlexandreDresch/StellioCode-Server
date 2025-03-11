package com.stelliocode.backend.entity;

public enum ProjectRole {
    BACKEND,
    FRONTEND,
    FULLSTACK,
    QA,
    PM;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
