package com.punithan_library.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.punithan_library.Payload.DTO.SubscriptionDTO;

public interface SubscriptionService {

    SubscriptionDTO subscribe(SubscriptionDTO dto);
    SubscriptionDTO getUsersActiveSubscription(Long userId);
    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason);
    SubscriptionDTO activeSubscription(Long subscriptionId, Long paymentId);
    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);
    
}
