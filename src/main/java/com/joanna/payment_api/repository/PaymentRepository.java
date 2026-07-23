package com.joanna.payment_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joanna.payment_api.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}