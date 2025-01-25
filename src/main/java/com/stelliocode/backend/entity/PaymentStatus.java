package com.stelliocode.backend.entity;

public enum PaymentStatus {
    PENDING,
    PAID,
    FAILED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}