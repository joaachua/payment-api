package com.joanna.payment_api.service;

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
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = new Payment(
                request.amount(),
                request.currency());

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResponse.from(savedPayment);
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
}