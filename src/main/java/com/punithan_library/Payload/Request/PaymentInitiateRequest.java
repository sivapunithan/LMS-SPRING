package com.punithan_library.Payload.Request;

import java.math.BigDecimal;

import com.punithan_library.Domain.PaymentGateway;
import com.punithan_library.Domain.PaymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class PaymentInitiateRequest {
    
    @NotNull(message = "User ID is required")
    private long userId;

    private Long bookLoanId;

    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    @NotNull(message = "Payment Gateway is required")
    private PaymentGateway paymentGateway;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotNull(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters long")
    private String currency;

    @NotNull(message = "Description is required")
    @Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters long")
    private String description;

    private Long fineId;
    private Long subscriptionId;

    private String successUrl;
    private String cancelUrl;
}
