package com.punithan_library.Payload.DTO;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Domain.BookLoanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BookLoanDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookId;
    private String bookTitle;
    private String bookISBN;
    private String bookAuthor;
    private String bookCoverImage;
    private BookLoanType type;
    private BookLoanStatus status;
    private LocalDate checkOutDate;
    private LocalDate dueDate;
    private Long remainingDays;
    private LocalDate returnDate;
    private Integer renewalCount;
    private Integer maxRenewals;
    private BigDecimal fineAmount;
    private Boolean finePaid;
    private String notes;
    private Boolean isOverdue;
    private Integer overDueDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
