package com.punithan_library.Service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.punithan_library.Domain.PaymentGateway;
import com.punithan_library.Domain.PaymentType;
import com.punithan_library.Entity.SubscriptionEntity;
import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Mapper.SubscriptionPlanMapper;
import com.punithan_library.Mapper.SubscriptonMapper;
import com.punithan_library.Payload.DTO.SubscriptionDTO;
import com.punithan_library.Payload.Request.PaymentInitiateRequest;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import com.punithan_library.Repository.SubscriptionPlanRepo;
import com.punithan_library.Repository.SubscriptionRepository;
import com.punithan_library.Service.PaymentService;
import com.punithan_library.Service.SubscriptionService;
import com.punithan_library.Service.UserService;

@Service
public class SubscriptionImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionPlanRepo subscriptionPlanRepository;
    @Autowired
    private SubscriptonMapper subscriptionMapper;
    @Autowired
    private PaymentService paymentService;

    @Override
    public PaymentInitiateResponse subscribe(SubscriptionDTO dto) throws Exception {
        
        UserEntity user = userService.getCurrentUser();
        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new Exception("Subscription plan not found"));
        
        SubscriptionEntity subscription = subscriptionMapper.toEntity(dto, plan, user);
        subscription.initializedFromPlan();
        subscription.setIsActive(false);

        // Create Payment
        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);
        PaymentInitiateRequest paymentInitiateRequest = PaymentInitiateRequest.builder()
            .userId(user.getId())
            .currency("INR")
            .subscriptionId(savedSubscription.getId())
            .amount(plan.getPrice())
            .paymentType(PaymentType.MEMBERSHIP)
            .paymentGateway(PaymentGateway.RAZORPAY)
            .description("Subscription for " + plan.getName())
            .build();
            
        return paymentService.initiatePayment(paymentInitiateRequest);

    }

    @Override
    public SubscriptionDTO getUsersActiveSubscription(Long userId) {
        UserEntity user = userService.getCurrentUser();
        SubscriptionEntity subscription = subscriptionRepository.findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No active subscription found"));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws Exception {

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new Exception("Subscription not found"));

        if (!subscription.getIsActive()){
            throw new Exception("Subscription is not active");
        }
        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancelReason(reason != null ? reason : "Cancelled by User");
        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws Exception {
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new Exception("Subscription not found"));
        // Verify Payment (todo)
        subscription.setIsActive(true);
        
        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        
        // List<SubscriptionEntity> subscriptions = (List<SubscriptionEntity>) subscriptionRepository.findAll(pageable);
        // return subscriptions.stream().map(subscriptionMapper::toDTO).collect(Collectors.toList());

        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subscriptionEntities);
    }

    @Override
    public void deactivateExpiredSubscriptions() throws Exception {
        
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findExpiredSubscriptions(LocalDate.now());
        for (SubscriptionEntity subscription : subscriptions) {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }
    
}
