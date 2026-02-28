package com.punithan_library.Event.Publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.punithan_library.Entity.PaymentEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;

    public void publishPaymentSuccessEvent(PaymentEntity payment) {
        eventPublisher.publishEvent(payment);
    }
    
}
