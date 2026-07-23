package com.joanna.payment_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePaymentRequest(

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount,

        @NotBlank(message = "Currency is required") @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must contain 3 uppercase letters") String currency

) {
}