package com.joanna.payment_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.joanna.payment_api.dto.CreatePaymentRequest;
import com.joanna.payment_api.dto.PaymentResponse;
import com.joanna.payment_api.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(
            @PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PostMapping("/{id}/confirm")
    public PaymentResponse confirmPayment(
            @PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }

    @PostMapping("/{id}/fail")
    public PaymentResponse failPayment(
            @PathVariable Long id) {
        return paymentService.failPayment(id);
    }

    @PostMapping("/{id}/refund")
    public PaymentResponse refundPayment(
            @PathVariable Long id) {
        return paymentService.refundPayment(id);
    }
}