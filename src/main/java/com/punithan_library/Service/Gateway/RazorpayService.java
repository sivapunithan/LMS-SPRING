package com.punithan_library.Service.Gateway;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.punithan_library.Domain.PaymentType;
import com.punithan_library.Entity.PaymentEntity;
import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Payload.Response.PaymentLinkResponse;
import com.punithan_library.Repository.SubscriptionPlanRepo;
import com.punithan_library.Service.SubscriptionPlanService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayService {

    @Value("${razorpay.keyId}")
    private String razorPayKeyId;

    @Value("${razorpay.keySecret}")
    private String razorPayKeySecret;

    @Value("${razorpay.callback.base-url:http://localhost:5173}")
    private String callbackBaseUrl;

    @Autowired
    private SubscriptionPlanRepo planRepository;

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    public PaymentLinkResponse createPaymentLink(UserEntity user, PaymentEntity payment) {

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorPayKeyId, razorPayKeySecret);
            
            Long amountInPaise = payment.getAmount() * 100;

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaise);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", payment.getDescription());

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            customer.put("contact", user.getPhone());

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", user.getPhone() != null && !user.getPhone().isBlank());
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            String successUrl = callbackBaseUrl + "/payment-success/" + payment.getId();

            paymentLinkRequest.put("callback_url", successUrl);
            paymentLinkRequest.put("callback_method", "get");

            JSONObject notes = new JSONObject();
            notes.put("userId", user.getId());
            notes.put("payment_Id", payment.getId());

            if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
                notes.put("subscription_id", payment.getSubscription().getId());
                notes.put("plan", payment.getSubscription().getPlan().getPlanCode());
                notes.put("type", PaymentType.MEMBERSHIP);
            } else if (payment.getPaymentType() == PaymentType.FINE) {
                // TODO: Add fine id
                // notes.put("fine_id", payment.getFine().getId());
                notes.put("type", PaymentType.FINE);
            }
            paymentLinkRequest.put("notes", notes);

            PaymentLink razorpayPaymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = razorpayPaymentLink.get("id");
            String paymentLinkUrl = razorpayPaymentLink.get("short_url");

            PaymentLinkResponse response = new PaymentLinkResponse();
            response.setPayment_link_url(paymentLinkUrl);
            response.setPayment_link_id(paymentLinkId);

            return response;

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment link", e);
        }
    }

    public JSONObject fetchPaymentDetails(String paymentId) throws Exception{
        try {

            RazorpayClient razorpayClient = new RazorpayClient(razorPayKeyId, razorPayKeySecret);
            com.razorpay.Payment payment = razorpayClient.payments.fetch(paymentId);

            return payment.toJson();

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to fetch payment details: " + e.getMessage(), e);
        }
    }

    public Boolean isValidPayment(String paymentId){

        try{
            JSONObject paymentDetails = fetchPaymentDetails(paymentId);

            String status = paymentDetails.optString("status");
            Long amount = paymentDetails.optLong("amount");
            Long amountInRupees = amount / 100;

            JSONObject notes = paymentDetails.optJSONObject("notes");
            String paymentType = notes.optString("type");

            if (!"captured".equals(status)) {
                return false;
            }

            if (paymentType.equals(PaymentType.MEMBERSHIP.toString())) {
                
                String planCode = notes.optString("plan");
                SubscriptionPlanEntity plan = subscriptionPlanService.getSubscriptionPlanByCode(planCode);
                return amountInRupees == (plan.getPrice());

            } else if (paymentType.equals(PaymentType.FINE.toString())) {
                Long fineId = notes.optLong("fine_id");

                // todo 

                // Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found"));
                // return amountInRupees == (fine.getAmount());
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify payment", e);
        }
    }
}
