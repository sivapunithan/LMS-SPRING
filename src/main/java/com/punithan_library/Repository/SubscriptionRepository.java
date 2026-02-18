package com.punithan_library.Repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.punithan_library.Entity.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.user.id = :userId AND s.isActive = true AND s.endDate >= :today")
   Optional<SubscriptionEntity> findActiveSubscriptionByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);
}
