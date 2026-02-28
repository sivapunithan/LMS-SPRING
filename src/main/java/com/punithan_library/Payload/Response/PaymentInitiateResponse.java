package com.punithan_library.Payload.Response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PaymentInitiateResponse {

    private Long paymentId;
    private String transactionId;
    private String razorPayOrderId;
    private Long amount;
    private String currency;
    private String description;

    private String checkoutUrl;
    private String message;
    private Boolean success;

}
