package com.punithan_library.Payload.DTO;

import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FineDTO {

    private Long id;
    @NotNull(message = "Book loan id is mandatory")
    private Long bookLoanId;
    private String bookTitle;
    private String bookIsbn;
    @NotNull(message = "User Id is mandatory")
    private Long userId;
    private String userName;
    private String userEmail;
    @NotNull(message = "Fine type is mandatory")
    private FineType type;
    @NotNull(message = "Fine Amount is mandatory")
    @PositiveOrZero(message = "Amount cant be negative")
    private Long amount;
    @PositiveOrZero (message = "Amoutn paid cannot be negative")
    private Long paidAmount;
    @NotNull(message = "Fine Amount is mandatory")
    private FineStatus status;
    private String reason;
    private String notes;

    // Waiver information
    private Long waiverUserId;
    private String waiverUserName;
    private LocalDateTime waivedAt;
    private String waiverReason;

    // Payment information
    private LocalDateTime paidAt;
    private Long processedByUserId;
    private String processedUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String transactionId;
}
