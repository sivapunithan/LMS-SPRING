package com.punithan_library.Controller;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Payload.DTO.BookLoanDTO;
import com.punithan_library.Payload.Request.BookLoanSearchRequest;
import com.punithan_library.Payload.Request.CheckInRequest;
import com.punithan_library.Payload.Request.CheckOutRequest;
import com.punithan_library.Payload.Request.RenewalRequest;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-loans")
public class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOutBook(@Valid @RequestBody CheckOutRequest request) throws Exception {

            BookLoanDTO bookLoanDTO = bookLoanService.checkOutBook(request);
            return ResponseEntity.status(HttpStatus.OK).body(bookLoanDTO);
    }

    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<?> checkOutBookForUser(@PathVariable Long userId, @Valid @RequestBody CheckOutRequest request) throws Exception {

        BookLoanDTO bookLoanDTO = bookLoanService.checkOutBookForUser(userId, request);
        return  ResponseEntity.ok(bookLoanDTO);
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@Valid @RequestBody CheckInRequest request) throws Exception {

        BookLoanDTO bookLoanDTO = bookLoanService.checkInBook(request);
        return ResponseEntity.ok(bookLoanDTO);
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renew(@Valid @RequestBody RenewalRequest request) throws Exception {

        BookLoanDTO bookLoanDTO = bookLoanService.renewCheckOut(request);
        return ResponseEntity.ok(bookLoanDTO);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookLoans(@RequestParam(required = false)BookLoanStatus status,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size){

        PageResponse<BookLoanDTO> bookLoanDTOPageResponse = bookLoanService.getMyBookLoans(status, page, size);
        return ResponseEntity.ok(bookLoanDTOPageResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllBookLoans(@RequestBody BookLoanSearchRequest request){

        PageResponse<BookLoanDTO> bookLoanDTOPageResponse = bookLoanService.getBookLoans(request);
        return ResponseEntity.ok(bookLoanDTOPageResponse);
    }

    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBookLoans(){
        int updateCount = bookLoanService.updateOverdueBookLoans();
        return ResponseEntity.ok(new ApiResponse("Overdue book loans are upadted ", true));
    }
}
