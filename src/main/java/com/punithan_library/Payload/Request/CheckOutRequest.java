package com.punithan_library.Payload.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @Min(value = 1, message = "Checkout days must be at least 1")
    private Integer checkOutDays = 14;

    private String notes;
}
