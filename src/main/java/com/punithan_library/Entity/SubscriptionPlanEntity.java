package com.punithan_library.Entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String planCode;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    private Integer duration;

    private Integer durationDays;

    @Column(nullable = false)
    private Long price;

    private String currency = "INR";

    @Column(nullable = false)
    @Positive(message = "Max books must be poitive")
    private Integer maxBooksAllowed;

    @Column(nullable = false)
    @Positive(message = "Max days must be poitive")
    private Integer maxDaysPerBook;

    private Integer displayOrder = 0;

    private Boolean isActive = true;
    private Boolean isFeatured = false;

    private String bookText;
    private String adminNotes;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
