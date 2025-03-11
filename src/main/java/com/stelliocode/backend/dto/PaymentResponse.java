package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String projectName;
    private BigDecimal amount;
    private String paymentStatus;
}