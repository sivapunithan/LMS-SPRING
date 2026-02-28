package com.punithan_library.Service;

import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Payload.DTO.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {

    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO request) throws Exception;

    SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO request) throws Exception;

    void deleteSubscriptionPlan(Long planId) throws Exception;

    List<SubscriptionPlanDTO> getSubscriptionPlans(Long planId);

    SubscriptionPlanEntity getSubscriptionPlanByCode(String planCode);
}
