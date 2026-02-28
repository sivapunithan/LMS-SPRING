package com.punithan_library.Event.Listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.punithan_library.Entity.PaymentEntity;
import com.punithan_library.Service.SubscriptionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final SubscriptionService subscriptionService;
    
    @Async
    @EventListener
    @Transactional
    public void handlePaymentSuccess(PaymentEntity payment) throws Exception {
        
        switch (payment.getPaymentType()) {
            case FINE:
                case LOST_BOOK_PENALTY:
                    case DAMAGED_BOOK_PENALTY:
                        break;

            case MEMBERSHIP:
                subscriptionService.activateSubscription(payment.getSubscription().getId(), payment.getUser().getId());
                break;

            case REFUND:
                break;
        }
    }
    
}
