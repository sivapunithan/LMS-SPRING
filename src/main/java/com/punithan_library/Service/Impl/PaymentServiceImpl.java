package com.punithan_library.Service.Impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.punithan_library.Domain.PaymentGateway;
import com.punithan_library.Domain.PaymentStatus;
import com.punithan_library.Entity.PaymentEntity;
import com.punithan_library.Entity.SubscriptionEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Event.Publisher.PaymentEventPublisher;
import com.punithan_library.Mapper.PaymentMapper;
import com.punithan_library.Payload.DTO.PaymentDTO;
import com.punithan_library.Payload.Request.PaymentInitiateRequest;
import com.punithan_library.Payload.Request.PaymentVerifyRequest;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import com.punithan_library.Payload.Response.PaymentLinkResponse;
import com.punithan_library.Repository.PaymentRepository;
import com.punithan_library.Repository.SubscriptionRepository;
import com.punithan_library.Repository.UserRepository;
import com.punithan_library.Service.PaymentService;
import com.punithan_library.Service.Gateway.RazorpayService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RazorpayService razorPayService;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private PaymentEventPublisher paymentEventPublisher;

    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws Exception {
        UserEntity user = userRepository.findById(request.getUserId()).get();

        PaymentEntity payment = new PaymentEntity();
        payment.setUser(user);
        payment.setPaymentType(request.getPaymentType());
        payment.setGateWay(request.getPaymentGateway());
        payment.setAmount(request.getAmount());

        payment.setDescription(request.getDescription());
        payment.setTransactionId("TXN_" + UUID.randomUUID());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setInitaiatedAt(LocalDateTime.now());

        if (request.getSubscriptionId() != null) {
            SubscriptionEntity subscription = subscriptionRepository.findById(request.getSubscriptionId())
                    .orElseThrow(() -> new Exception("Subscription not found"));
            payment.setSubscription(subscription);
        }
        PaymentEntity savedPayment = paymentRepository.save(payment);

        PaymentInitiateResponse response = new PaymentInitiateResponse();

        if (request.getPaymentGateway() == PaymentGateway.RAZORPAY) {
            PaymentLinkResponse paymentLinkResponse = razorPayService.createPaymentLink(user, savedPayment);

            response = PaymentInitiateResponse.builder()
                    .paymentId(savedPayment.getId())
                    .transactionId(savedPayment.getTransactionId())
                    .razorPayOrderId(paymentLinkResponse.getPayment_link_id())
                    .amount(savedPayment.getAmount())
                    .currency(savedPayment.getCurrency())
                    .description(savedPayment.getDescription())
                    .checkoutUrl(paymentLinkResponse.getPayment_link_url())
                    .message("Payment link created successfully")
                    .success(true)
                    .build();

            savedPayment.setGatewayOrderId(paymentLinkResponse.getPayment_link_id());
        }
        savedPayment.setPaymentStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(savedPayment);

        // Payment Initiate Event
        paymentEventPublisher.publishPaymentSuccessEvent(savedPayment);
        return response;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception {
        
        JSONObject paymentDetails = razorPayService.fetchPaymentDetails(request.getRazorpayPaymentId());
        JSONObject notes = paymentDetails.optJSONObject("notes");
        Long paymentId = notes.optLong("payment_id");
        PaymentEntity payment = paymentRepository.findById(paymentId).get();

        Boolean isValidPayment = razorPayService.isValidPayment(request.getRazorpayPaymentId());

        if (PaymentGateway.RAZORPAY == payment.getGateWay()) {
            if (isValidPayment) {
                payment.setGatewayOrderId(request.getRazorpayPaymentId());
            }
        }
        if (isValidPayment){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);
        }
        // Publish Payment Success Event
        return paymentMapper.toDto(payment);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        Page<PaymentEntity> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toDto);
    }

}
