package com.punithan_library.Mapper;

import org.springframework.stereotype.Component;

import com.punithan_library.Entity.SubscriptionPlanEntity;
import com.punithan_library.Payload.DTO.SubscriptionPlanDTO;

@Component
public class SubscriptionPlanMapper {
    public SubscriptionPlanDTO toDto(SubscriptionPlanEntity plan){

        if (plan == null) return null;

        SubscriptionPlanDTO dto = SubscriptionPlanDTO.builder()
        .id(plan.getId())
        .planCode(plan.getPlanCode())
        .name(plan.getName())
        .description(plan.getDescription())
        .duration(plan.getDuration())
        .durationDays(plan.getDurationDays())
        .price(plan.getPrice())
        .currency(plan.getCurrency())
        .maxBooksAllowed(plan.getMaxBooksAllowed())
        .maxDaysPerBook(plan.getMaxDaysPerBook())
        .displayOrder(plan.getDisplayOrder())
        .isActive(plan.getIsActive())
        .isFeatured(plan.getIsFeatured())
        .bookText(plan.getBookText())
        .adminNotes(plan.getAdminNotes())
        .createdAt(plan.getCreatedAt())
        .updatedAt(plan.getUpdatedAt())
        .createdBy(plan.getCreatedBy())
        .updatedBy(plan.getUpdatedBy())
        .build();

        return dto;
    }

    public SubscriptionPlanEntity toEntity(SubscriptionPlanDTO dto){
        if (dto == null) return null;

        SubscriptionPlanEntity plan = SubscriptionPlanEntity.builder()
        .id(dto.getId())
        .planCode(dto.getPlanCode())
        .name(dto.getName())
        .description(dto.getDescription())
        .duration(dto.getDuration())
        .durationDays(dto.getDurationDays())
        .price(dto.getPrice())
        .currency(dto.getCurrency())
        .maxBooksAllowed(dto.getMaxBooksAllowed())
        .maxDaysPerBook(dto.getMaxDaysPerBook())
        .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
        .isFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)
        .bookText(dto.getBookText())
        .adminNotes(dto.getAdminNotes())
        .createdAt(dto.getCreatedAt())
        .updatedAt(dto.getUpdatedAt())
        .createdBy(dto.getCreatedBy())
        .updatedBy(dto.getUpdatedBy())
        .build();

        return plan;
    }

    public void updateEntityFromDto(SubscriptionPlanEntity entity, SubscriptionPlanDTO dto){
        if (dto == null || entity == null) return;

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getDuration() != null) entity.setDuration(dto.getDuration());
        if (dto.getDurationDays() != null) entity.setDurationDays(dto.getDurationDays());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getCurrency() != null) entity.setCurrency(dto.getCurrency());
        if (dto.getMaxBooksAllowed() != null) entity.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        if (dto.getMaxDaysPerBook() != null) entity.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        if (dto.getDisplayOrder() != null) entity.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getIsActive() != null) entity.setIsActive(dto.getIsActive());
        if (dto.getIsFeatured() != null) entity.setIsFeatured(dto.getIsFeatured());
        if (dto.getBookText() != null) entity.setBookText(dto.getBookText());
        if (dto.getAdminNotes() != null) entity.setAdminNotes(dto.getAdminNotes());
        if (dto.getUpdatedAt() != null) entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getUpdatedBy() != null) entity.setUpdatedBy(dto.getUpdatedBy());
    }
}
