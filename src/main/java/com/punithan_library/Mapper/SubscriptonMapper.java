package com.punithan_library.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Component;

import com.punithan_library.Entity.SubscriptionEntity;
import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Exception.SubscriptionException;
import com.punithan_library.Payload.DTO.SubscriptionDTO;
import com.punithan_library.Repository.SubscriptionPlanRepo;
import com.punithan_library.Repository.UserRepository;

@Component
public class SubscriptonMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionPlanRepo subscriptionPlanRepo;

    public SubscriptionDTO toDTO(SubscriptionEntity entity){
        if (entity == null) return null;
        SubscriptionDTO dto = new SubscriptionDTO();
        if (entity.getUser() != null){
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getFullName());
            dto.setUserEmail(entity.getUser().getEmail());
        }
        if (entity.getPlan() != null){
             dto.setPlanId(entity.getPlan().getId());
        }       
        dto.setPlanName(entity.getPlanName());
        dto.setPlanCode(entity.getPlanCode());
        dto.setPrice(entity.getPrice());

        dto.setCurrency(entity.getPlan().getCurrency());
        dto.setMaxBooksAllowed(entity.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(entity.getMaxDaysPerBook());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsActive(entity.getIsActive());
        dto.setAutoRenew(entity.getAutoRenew());
        dto.setCancelledAt(entity.getCancelledAt());
        dto.setCancelReason(entity.getCancelReason());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setDaysRemaining(entity.getRemainingDays());
        dto.setIsValid(entity.isValid());
        dto.setIsExpired(entity.isExpired());
        return dto;
    }

    public SubscriptionEntity toEntity(SubscriptionDTO dto){
        if (dto == null) return null;
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setId(dto.getId());

        if (dto.getUserId() != null){
            UserEntity user = userRepository.findById(dto.getUserId())
                            .orElseThrow(()-> new SubscriptionException("User not found with ID: " + dto.getUserId()));
            entity.setUser(user);
        }
        if (dto.getPlanId() != null){
            SubscriptionPlanEntity plan = subscriptionPlanRepo.findById(dto.getPlanId())
                            .orElseThrow(()-> new SubscriptionException("Plan not found with ID: " + dto.getPlanId()));
            entity.setPlan(plan);
        }
        entity.setPlanName(dto.getPlanName());
        entity.setPlanCode(dto.getPlanCode());
        entity.setPrice(dto.getPrice());
        
        entity.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        entity.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsActive(dto.getIsActive());
        entity.setAutoRenew(dto.getAutoRenew());
        entity.setCancelledAt(dto.getCancelledAt());
        entity.setCancelReason(dto.getCancelReason());
        entity.setNotes(dto.getNotes());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    public List<SubscriptionDTO> toDTOList(List<SubscriptionEntity> entities){
        if (entities == null) return null;
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
