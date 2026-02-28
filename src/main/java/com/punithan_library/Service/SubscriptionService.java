package com.punithan_library.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.punithan_library.Payload.DTO.SubscriptionDTO;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;

public interface SubscriptionService {

    PaymentInitiateResponse subscribe(SubscriptionDTO dto) throws Exception;
    SubscriptionDTO getUsersActiveSubscription(Long userId) throws Exception;
    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws Exception;
    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws Exception;
    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);
    void deactivateExpiredSubscriptions() throws Exception;
    
}
