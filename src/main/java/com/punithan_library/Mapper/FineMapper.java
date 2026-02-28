package com.punithan_library.Mapper;

import com.punithan_library.Entity.FineEntity;
import com.punithan_library.Payload.DTO.FineDTO;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {

    public FineDTO toDTO(FineEntity fine){

        if (fine == null) return null;
        FineDTO dto = new FineDTO();
        dto.setId(fine.getId());

        // Book loan information
        if (fine.getBookLoan() != null){
            dto.setBookLoanId(fine.getBookLoan().getId());
            if (fine.getBookLoan().getBook() != null){
                dto.setBookTitle(fine.getBookLoan().getBook().getTitle());
                dto.setBookIsbn(fine.getBookLoan().getBook().getIsbn());
            }
        }

        // user information
        if (fine.getUser() != null){
            dto.setUserId(fine.getUser().getId());
            dto.setUserEmail(fine.getUser().getEmail());
            dto.setUserName(fine.getUser().getFullName());
        }

        dto.setType(fine.getType());
        dto.setAmount(fine.getAmount());
        dto.setStatus(fine.getStatus());
        dto.setReason(fine.getReason());
        dto.setNotes(fine.getNote());

        // Waiver information
        if (fine.getWaivedBy() != null){
            dto.setWaiverUserId(fine.getWaivedBy().getId());
            dto.setWaiverUserName(fine.getWaivedBy().getFullName());
        }
        dto.setWaivedAt(fine.getWaivedAt());
        dto.setWaiverReason(fine.getWaivedReason());

        // Payment information
        dto.setPaidAt(fine.getPaidAt());
        if (fine.getProcessedBy() != null){
            dto.setProcessedByUserId(fine.getProcessedBy().getId());
            dto.setProcessedUserName(fine.getProcessedBy().getFullName());
        }
        dto.setTransactionId(fine.getTransactionId());
        dto.setCreatedAt(fine.getCreatedAt());
        dto.setUpdatedAt(fine.getUpdatedAt());

        return dto;

    }
}
