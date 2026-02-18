package com.punithan_library.Payload.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriptionDTO {
   
    private Long id;
    @NotNull(message = "User id is required")
    private Long userId;
    private String userName;
    private String userEmail;

    @NotNull(message = "Plan id is required")
    private Long planId;
    private String planName;
    private String planCode;
    private BigDecimal price;
    private String currency;
    
    private Integer maxBooksAllowed;
    private Integer maxDaysPerBook;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Boolean autoRenew;
    private LocalDateTime cancelledAt;
    private String cancelReason;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long daysRemaining;
    private Boolean isValid;
    private Boolean isExpired;
}
