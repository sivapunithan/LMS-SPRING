package com.punithan_library.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PaymentVerifyRequest {
    
    private String razorpayPaymentId;
    
    private String stripePaymentIntentId;
    private String striprPayentIndentStatus;
}
