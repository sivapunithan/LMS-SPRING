package com.punithan_library.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentLinkResponse {
    
    private String payment_link_url;
    private String payment_link_id;
}
