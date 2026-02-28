package com.punithan_library.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.punithan_library.Payload.DTO.SubscriptionDTO;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import com.punithan_library.Service.SubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<PaymentInitiateResponse> subscribe(@Valid @RequestBody SubscriptionDTO dto) throws Exception {
        return ResponseEntity.ok(subscriptionService.subscribe(dto));
    }

    @GetMapping("/user/active")
    public ResponseEntity<SubscriptionDTO> getUsersActiveSubscription(@RequestParam(required = false) Long userId) throws Exception {
        return ResponseEntity.ok(subscriptionService.getUsersActiveSubscription(userId));
    }

    @PutMapping("/cancel/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(@PathVariable Long subscriptionId, @RequestParam(required = false) String reason) throws Exception {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId, reason));
    }

    @PutMapping("/activate")
    public ResponseEntity<SubscriptionDTO> activeSubscription(@RequestParam Long subscriptionId, @RequestParam Long paymentId) throws Exception {
        return ResponseEntity.ok(subscriptionService.activateSubscription(subscriptionId, paymentId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(pageable));
    }

    @PutMapping("/admin/deactivate-expired")
    public ResponseEntity<?> deactivateExpiredSubscriptions() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        subscriptionService.deactivateExpiredSubscriptions();
        ApiResponse response = new ApiResponse("Task Done !!", true);
        return ResponseEntity.ok(response);
    }
}
