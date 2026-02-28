package com.punithan_library.Entity;

import com.punithan_library.Domain.ReservatonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private BookEntity book;
    private ReservatonStatus status = ReservatonStatus.PENDING;
    private LocalDateTime reservedAt;
    private LocalDateTime availableAt;
    private LocalDateTime availableUntil;
    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    @Column(name = "queue_position")
    private Integer queuePosition;
    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent = false;
    private String notes;

    @CreationTimestamp
    @Column(name = "craeted_at", nullable = false, updatable = false)
    private  LocalDateTime craetedAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
