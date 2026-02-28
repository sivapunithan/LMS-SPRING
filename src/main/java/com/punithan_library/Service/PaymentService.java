package com.punithan_library.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.punithan_library.Payload.DTO.PaymentDTO;
import com.punithan_library.Payload.Request.PaymentInitiateRequest;
import com.punithan_library.Payload.Request.PaymentVerifyRequest;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;

public interface PaymentService {
    
    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws Exception;

    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception;

    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
