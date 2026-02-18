package com.punithan_library.Controller;

import com.punithan_library.Payload.DTO.SubscriptionPlanDTO;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Service.SubscriptionPlanService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService planService;

    @PostMapping("/admin/create")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO request) throws Exception {
        SubscriptionPlanDTO createdPlan = planService.createSubscriptionPlan(request);
        return ResponseEntity.ok(createdPlan);
    }

    @PutMapping("/admin/{planId}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(@PathVariable Long planId, @RequestBody SubscriptionPlanDTO request) throws Exception {
        SubscriptionPlanDTO updatedPlan = planService.updateSubscriptionPlan(planId, request);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/admin/delete/{planId}")
    public ResponseEntity<?> deleteSubscriptionPlan(@PathVariable Long planId) throws Exception {
        planService.deleteSubscriptionPlan(planId);
        ApiResponse response = new ApiResponse("Plan deleted successfully", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getSubscriptionPlans() {
        List<SubscriptionPlanDTO> plans = planService.getSubscriptionPlans(null);
        return ResponseEntity.ok(plans);
    }
}
