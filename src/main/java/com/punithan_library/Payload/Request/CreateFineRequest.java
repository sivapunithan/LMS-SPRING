package com.punithan_library.Payload.Request;

import com.punithan_library.Domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFineRequest {

    @NotNull(message = "Book Loan ID is mandatory")
    private Long bookLoanId;
    @NotNull(message = "Fine type is mandatory")
    private FineType type;
    @NotNull(message = "Fine amount is mandatory")
    @PositiveOrZero(message = "Fine amount must be positive")
    private Long amount;
    private String reason;
    private String notes;
}
