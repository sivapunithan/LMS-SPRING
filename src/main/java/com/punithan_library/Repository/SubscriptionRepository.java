package com.punithan_library.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.punithan_library.Entity.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

   @Query("SELECT s FROM SubscriptionEntity s WHERE s.user.id = :userId AND s.isActive = true AND s.endDate >= :today")
   Optional<SubscriptionEntity> findActiveSubscriptionByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);

   @Query("SELECT s FROM SubscriptionEntity s WHERE s.user.id = :userId AND s.isActive = true AND s.endDate >= :today")
   List<SubscriptionEntity> findActiveSubscriptionsByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);

   @Query("SELECT s FROM SubscriptionEntity s WHERE s.user.id = :userId AND s.isActive = true AND s.endDate >= :today")
   List<SubscriptionEntity> findExpiredSubscriptions(@Param("today") LocalDate today);

}
