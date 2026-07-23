package com.joanna.payment_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.joanna.payment_api.dto.CreatePaymentRequest;
import com.joanna.payment_api.dto.PaymentResponse;
import com.joanna.payment_api.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Create and manage payment transactions")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a payment", description = "Creates a new payment with PENDING status")
    public PaymentResponse createPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,

            @Valid @RequestBody CreatePaymentRequest request) {

        return paymentService.createPayment(
                idempotencyKey,
                request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment", description = "Get an existing payment")
    public PaymentResponse getPayment(
            @PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm a payment", description = "Confirm an existing payment")
    public PaymentResponse confirmPayment(
            @PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }

    @PostMapping("/{id}/fail")
    @Operation(summary = "Mark payment as failed", description = "Failed a pending payment")
    public PaymentResponse failPayment(
            @PathVariable Long id) {
        return paymentService.failPayment(id);
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund a payment", description = "Refund a confirmed payment")
    public PaymentResponse refundPayment(
            @PathVariable Long id) {
        return paymentService.refundPayment(id);
    }
}