package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.PaymentResponse;
import com.stelliocode.backend.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${app.cors.allowed-origin}")
    private String origin;

    private final PaymentService paymentService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @GetMapping("/{clientId}/{projectId}")
    public ResponseEntity<?> getPaymentByIdClient(
            @PathVariable String clientId,
            @PathVariable UUID projectId) {

        PaymentResponse response = paymentService.getPaymentByIdAndClientGoogleId(projectId, clientId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-session/{projectId}/{paymentId}")
    public ResponseEntity<String> createCheckoutSession(@PathVariable UUID projectId, @PathVariable String paymentId) {
        String DOMAIN = origin;

        PaymentResponse payment = paymentService.getPaymentById(paymentId);

        if (payment.getPaymentStatus().equals("PAID")) {
            return ResponseEntity.badRequest().body("The payment is already marked as PAID.");
        }

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(DOMAIN + "/acompanhamento/" + projectId + "/success?paymentId=" + payment.getId())
                    .setCancelUrl(DOMAIN + "/acompanhamento/" +  projectId)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("brl")
                                                    .setUnitAmount(payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // Converte para centavos
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(payment.getProjectName())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            paymentService.updateStripeTransactionId(paymentId, session.getId());

            return ResponseEntity.ok(session.getUrl());
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while creating checkout session: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam String paymentId) {
        paymentService.markPaymentAsPaid(paymentId);

        return ResponseEntity.ok("Payment successful!");
    }
}