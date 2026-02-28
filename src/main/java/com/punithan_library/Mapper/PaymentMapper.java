package com.punithan_library.Mapper;

import org.springframework.stereotype.Component;

import com.punithan_library.Entity.PaymentEntity;
import com.punithan_library.Payload.DTO.PaymentDTO;

import lombok.Builder;

@Component
@Builder
public class PaymentMapper {
    
    public PaymentDTO toDto(PaymentEntity payment){
        
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();

        dto.setId(payment.getId());
        
        // User Information
        if (payment.getUser() != null){
            dto.setUserId(payment.getUser().getId());
            dto.setUserName(payment.getUser().getFullName());
            dto.setUserEmail(payment.getUser().getEmail());
        }

        // Book Loan Information
        // if (payment.getBookLoan() != null){
        //     dto.setBookLoanId(payment.getBookLoan().getId());
        // }

        // Subscription Information
        if (payment.getSubscription() != null){
            dto.setSubscriptionId(payment.getSubscription().getId());
        }

        // Payment Information
        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setTransactionId(payment.getTransactionId());
        dto.setGatewayOrderId(payment.getGatewayOrderId());
        dto.setGatewaySignature(payment.getGatewaySignature());
        dto.setFailureReason(payment.getFailureReason());
        dto.setInitaiatedAt(payment.getInitaiatedAt());
        dto.setCompletedAt(payment.getCompletedAt());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        return dto;
    }
}
