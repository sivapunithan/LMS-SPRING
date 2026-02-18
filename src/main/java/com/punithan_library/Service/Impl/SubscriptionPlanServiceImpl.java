package com.punithan_library.Service.Impl;

import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Mapper.SubscriptionPlanMapper;
import com.punithan_library.Payload.DTO.SubscriptionPlanDTO;
import com.punithan_library.Repository.SubscriptionPlanRepo;
import com.punithan_library.Service.SubscriptionPlanService;
import com.punithan_library.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepo planRepository;
    @Autowired
    private SubscriptionPlanMapper planMapper;
    @Autowired
    private UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO dto) throws Exception {
        if (dto == null){
            throw new Exception("Plan is null");
        }
        if (planRepository.existsByPlanCode(dto.getPlanCode())){
            throw new Exception("Plan code already exists");
        }
        SubscriptionPlanEntity plan = planMapper.toEntity(dto);
        
        UserEntity currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());
        
        SubscriptionPlanEntity savedPlan = planRepository.save(plan);
        return planMapper.toDto(savedPlan);
        
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO request) throws Exception {
        SubscriptionPlanEntity existingPlan = planRepository.findById(planId).orElseThrow(()-> new Exception("Plan not found"));
        planMapper.updateEntityFromDto(existingPlan, request);

        UserEntity currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());
        
        SubscriptionPlanEntity updatedPlan = planRepository.save(existingPlan);
        return planMapper.toDto(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
        SubscriptionPlanEntity existingPlan = planRepository.findById(planId).orElseThrow(()-> new Exception("Plan not found"));
        planRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getSubscriptionPlans(Long planId) {
        List<SubscriptionPlanEntity> planList = planRepository.findAll();
        return planList.stream().map(planMapper :: toDto).collect(Collectors.toList());
    }
}
