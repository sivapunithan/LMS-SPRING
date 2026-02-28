package com.punithan_library.Payload.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.punithan_library.Domain.PaymentGateway;
import com.punithan_library.Domain.PaymentStatus;
import com.punithan_library.Domain.PaymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PaymentDTO {
    
    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    private String userName;
    private String userEmail;

    private Long bookLoanId;
    private Long subscriptionId;
    
    @NotNull(message = "Payment type is mandatory")
    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    @NotNull(message = "Payment gateway is mandatory")
    private PaymentGateway paymentGateway;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotNull(message = "Currency is mandatory")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters long")
    private String currency;

    private String transactionId;
    private String gatewayPaymentId;

    private String gatewayOrderId;
    private String gatewaySignature;
    
    private String failureReason;

    private Integer retryCount;
    

    private LocalDateTime initaiatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    

}
