package com.punithan_library.Service;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Payload.DTO.BookLoanDTO;
import com.punithan_library.Payload.Request.BookLoanSearchRequest;
import com.punithan_library.Payload.Request.CheckInRequest;
import com.punithan_library.Payload.Request.CheckOutRequest;
import com.punithan_library.Payload.Request.RenewalRequest;
import com.punithan_library.Payload.Response.PageResponse;
import org.springframework.data.domain.Page;

public interface BookLoanService {

    BookLoanDTO checkOutBook(CheckOutRequest Request) throws Exception;

    BookLoanDTO checkOutBookForUser(Long userId, CheckOutRequest Request) throws Exception;

    BookLoanDTO checkInBook(CheckInRequest request) throws Exception;

    BookLoanDTO renewCheckOut(RenewalRequest request) throws Exception;

    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size);

    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);

    Integer updateOverdueBookLoans();
}
