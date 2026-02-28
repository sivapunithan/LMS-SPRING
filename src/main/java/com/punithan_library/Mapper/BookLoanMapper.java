package com.punithan_library.Mapper;

import com.punithan_library.Entity.BookLoanEntity;
import com.punithan_library.Payload.DTO.BookLoanDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookLoanMapper {

    public BookLoanDTO toDTO(BookLoanEntity bookLoanEntity){

        if ( bookLoanEntity == null) return null;

        BookLoanDTO dto = new BookLoanDTO();
        dto.setId(bookLoanEntity.getId());

        // User information
        if (bookLoanEntity.getUser()!= null){
            dto.setUserId(bookLoanEntity.getUser().getId());
            dto.setUserName(bookLoanEntity.getUser().getFullName());
            dto.setUserEmail(bookLoanEntity.getUser().getEmail());
        }

        // Book information
        if (bookLoanEntity.getBook() != null){
            dto.setBookId(bookLoanEntity.getBook().getId());
            dto.setBookTitle(bookLoanEntity.getBook().getTitle());
            dto.setBookISBN(bookLoanEntity.getBook().getIsbn());
        }

        // Book loan details
        dto.setType(bookLoanEntity.getType());
        dto.setStatus(bookLoanEntity.getStatus());
        dto.setCheckOutDate(bookLoanEntity.getCheckOutDate());
        dto.setDueDate(bookLoanEntity.getDueDate());
        dto.setRemainingDays(ChronoUnit.DAYS.between(LocalDate.now(), bookLoanEntity.getDueDate()));
        dto.setReturnDate(bookLoanEntity.getReturnDate());
        dto.setRenewalCount(bookLoanEntity.getRenewalCount());
        dto.setMaxRenewals(bookLoanEntity.getMaxRenewals());

        return dto;
    }
}
