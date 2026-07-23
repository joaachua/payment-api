package com.joanna.payment_api.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joanna.payment_api.dto.CreatePaymentRequest;
import com.joanna.payment_api.dto.PaymentResponse;
import com.joanna.payment_api.entity.Payment;
import com.joanna.payment_api.enums.PaymentStatus;
import com.joanna.payment_api.exception.InvalidPaymentStateException;
import com.joanna.payment_api.exception.PaymentNotFoundException;
import com.joanna.payment_api.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(
            String idempotencyKey,
            CreatePaymentRequest request) {

        String requestHash = generateRequestHash(request);

        return paymentRepository
                .findByIdempotencyKey(idempotencyKey)
                .map(existingPayment -> {

                    if (!existingPayment
                            .getRequestHash()
                            .equals(requestHash)) {

                        throw new InvalidPaymentStateException(
                                "Idempotency key was already used "
                                        + "with a different request");
                    }

                    return PaymentResponse.from(existingPayment);
                })
                .orElseGet(() -> {
                    Payment payment = new Payment(
                            request.amount(),
                            request.currency().trim().toUpperCase(),
                            idempotencyKey,
                            requestHash);

                    Payment savedPayment = paymentRepository.save(payment);

                    return PaymentResponse.from(savedPayment);
                });
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long id) {

        Payment payment = findPayment(id);

        return PaymentResponse.from(payment);
    }

    @Transactional
    public PaymentResponse confirmPayment(Long id) {

        Payment payment = findPayment(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException(
                    "Only PENDING payments can be confirmed");
        }

        payment.markSuccessful();

        return PaymentResponse.from(payment);
    }

    @Transactional
    public PaymentResponse failPayment(Long id) {

        Payment payment = findPayment(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStateException(
                    "Only PENDING payments can be marked as failed");
        }

        payment.markFailed();

        return PaymentResponse.from(payment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long id) {

        Payment payment = findPayment(id);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidPaymentStateException(
                    "Only SUCCESS payments can be refunded");
        }

        payment.markRefunded();

        return PaymentResponse.from(payment);
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    private String generateRequestHash(
            CreatePaymentRequest request) {

        String requestData = request.amount().toPlainString()
                + "|"
                + request.currency().trim().toUpperCase();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(
                    requestData.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable",
                    exception);
        }
    }
}