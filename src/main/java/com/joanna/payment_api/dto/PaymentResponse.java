package com.joanna.payment_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.joanna.payment_api.entity.Payment;
import com.joanna.payment_api.enums.PaymentStatus;

public record PaymentResponse(
        Long id,
        String reference,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReference(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
    }
}