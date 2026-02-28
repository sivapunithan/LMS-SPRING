package com.punithan_library.Payload.Request;

import com.punithan_library.Domain.BookLoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CheckInRequest {

    @NotNull(message = "Book Loan ID is required")
    private Long bookLoanId;

    private BookLoanStatus condition = BookLoanStatus.RETURNED;   // Returned , lost ,  damaged
}
