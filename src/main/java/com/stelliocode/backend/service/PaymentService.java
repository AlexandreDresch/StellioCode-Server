package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.PaymentResponse;
import com.stelliocode.backend.entity.Payment;
import com.stelliocode.backend.entity.PaymentStatus;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.exception.NotFoundException;
import com.stelliocode.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createInitialPayment(Project project, BigDecimal amount) {
        Payment payment = Payment.builder()
                .project(project)
                .client(project.getClient())
                .amount(amount)
                .currency("BRL")
                .paymentStatus(PaymentStatus.PENDING)
                .stripeTransactionId(null)
                .build();

        return paymentRepository.save(payment);
    }

    public PaymentResponse getPaymentByIdAndClientGoogleId(UUID projectId, String googleId) {
        Payment payment = paymentRepository.findByProjectIdAndClientGoogleId(projectId, googleId)
                .orElseThrow(() -> new NotFoundException("Payment not found."));

        return new PaymentResponse(
                payment.getId(),
                payment.getProject().getTitle(),
                payment.getAmount(),
                payment.getPaymentStatus().toString()
        );
    }

    public PaymentResponse getPaymentByIdAndDeveloperId(UUID projectId, UUID developerId) {
        Payment payment = paymentRepository.findByProjectIdAndDeveloperId(projectId, developerId)
                .orElseThrow(() -> new NotFoundException("Payment not found."));

        return new PaymentResponse(
                payment.getId(),
                payment.getProject().getTitle(),
                payment.getAmount(),
                payment.getPaymentStatus().toString()
        );
    }

    public PaymentResponse getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        return new PaymentResponse(
                payment.getId(),
                payment.getProject().getTitle(),
                payment.getAmount(),
                payment.getPaymentStatus().toString()
        );
    }

    public void updateStripeTransactionId(String paymentId, String stripeTransactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        payment.setStripeTransactionId(stripeTransactionId);
        paymentRepository.save(payment);
    }

    public void markPaymentAsPaid(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("The Payment is already marked as PAID.");
        }

        payment.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByProjectId(UUID projectId) {
        return paymentRepository.findByProjectId(projectId);
    }
}