package com.punithan_library.Repository;

import com.punithan_library.Entity.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepo extends JpaRepository<SubscriptionPlanEntity, Long> {

    Boolean existsByPlanCode(String planCode);
}
