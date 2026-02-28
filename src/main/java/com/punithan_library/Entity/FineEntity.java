package com.punithan_library.Entity;

import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class FineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoanEntity bookLoan;

    private FineType type;

    @Column(nullable = false)
    private Long amount;

    private FineStatus status;

    @Column(length = 500)
    private String reason;

    @Column(length = 500)
    private String note;

    @ManyToOne
    private UserEntity waivedBy;

    @Column(name = "waived_at")
    private LocalDateTime waivedAt;

    @Column(name = "waived_reason", length = 500)
    private String waivedReason;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by_user_id")
    private UserEntity processedBy;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void applyPayment(Long paymentAmount){
        if (paymentAmount == null || paymentAmount <= 0){
            throw new IllegalArgumentException("Invalid payment amount");
        }
        // Update status based on amount paid
        this.status = FineStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void waive(UserEntity admin, String reason){

        this.status = FineStatus.WAIVED;
        this.waivedBy = admin;
        this.waivedAt = LocalDateTime.now();
        this.waivedReason = reason;
    }
}
