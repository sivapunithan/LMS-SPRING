package com.punithan_library.Entity;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Domain.BookLoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
@Table(name = "book_loans")
public class BookLoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private UserEntity user;

    @JoinColumn(nullable = false)
    @ManyToOne
    private BookEntity book;

    @Enumerated(EnumType.STRING)
    private BookLoanType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookLoanStatus status;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private Integer renewalCount = 0;

    @Column(nullable = false)
    private Integer maxRenewals = 2;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Boolean isOverdue = false;

    @Column(nullable = false)
    private Integer overdueDays = 0;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime UpdatedAt;

    public boolean isActive(){
        return status == BookLoanStatus.CHECKED_OUT || status == BookLoanStatus.OVERDUE;
    }

    public boolean canRenew(){
        return status == BookLoanStatus.CHECKED_OUT && !isOverdue && renewalCount < maxRenewals;
    }
}
