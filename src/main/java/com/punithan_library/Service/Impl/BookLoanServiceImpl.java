package com.punithan_library.Service.Impl;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Domain.BookLoanType;
import com.punithan_library.Entity.BookEntity;
import com.punithan_library.Entity.BookLoanEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Exception.BookException;
import com.punithan_library.Mapper.BookLoanMapper;
import com.punithan_library.Payload.DTO.BookLoanDTO;
import com.punithan_library.Payload.DTO.SubscriptionDTO;
import com.punithan_library.Payload.Request.BookLoanSearchRequest;
import com.punithan_library.Payload.Request.CheckInRequest;
import com.punithan_library.Payload.Request.CheckOutRequest;
import com.punithan_library.Payload.Request.RenewalRequest;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Repository.BookLoanRepository;
import com.punithan_library.Repository.BookRepository;
import com.punithan_library.Service.BookLoanService;
import com.punithan_library.Service.SubscriptionService;
import com.punithan_library.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookRepository bookRepository;
    private final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanDTO checkOutBook(CheckOutRequest request) throws Exception {

        UserEntity user = userService.getCurrentUser();
        return checkOutBookForUser(user.getId(), request);
    }

    @Override
    public BookLoanDTO checkOutBookForUser(Long userId, CheckOutRequest request) throws Exception {

        UserEntity user = userService.findById(userId);
        SubscriptionDTO subscription = subscriptionService.getUsersActiveSubscription(user.getId());
        BookEntity book = bookRepository.findById(request.getBookId()).orElseThrow(
                ()-> new BookException("Book not found with the id :"+ request.getBookId())
        );

        if (!book.getActive()){
            throw new BookException("Book is not active.");
        }
        if (book.getAvailableCopies() <= 0){
            throw  new BookException("Book has no available copies.");
        }
        // Check if the user already checked out this book
        if (bookLoanRepository.hasActiveCheckOut(userId, book.getId())){
            throw   new BookException("Book has already checked out the book.");
        }
        // Check the user active checkout limit
        Long activeCheckouts = bookLoanRepository.countActiveBookLoansByUser(userId);
        int maxBooksAllowed = subscription.getMaxBooksAllowed();

        if (activeCheckouts >= maxBooksAllowed){
            throw   new BookException("Book has already checked out the book.");
        }
        // check over due books
        Long overDueCount = bookLoanRepository.countOverDueBookLoanByUser(userId);
        if (overDueCount > 0){
            throw   new BookException("Return the overDue books ");
        }
        // create book loan
        BookLoanEntity bookLoan = BookLoanEntity.builder()
                .user(user)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKED_OUT)
                .checkOutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(request.getCheckOutDays()))
                .renewalCount(0)
                .maxRenewals(2)
                .notes(request.getNotes())
                .isOverdue(false)
                .overdueDays(0)
                .build();
        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);
        // Save book loan
        BookLoanEntity saveBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(saveBookLoan);
    }

    @Override
    public BookLoanDTO checkInBook(CheckInRequest request) throws Exception {

        // Validate Book loan
        BookLoanEntity bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(
                ()-> new Exception("Book Loan not found")
        );
        // Check already returned
        if (!bookLoan.isActive()){
            throw new Exception("Book Loan is not active.");
        }
        bookLoan.setReturnDate(LocalDate.now());

        BookLoanStatus condition = request.getCondition();
        if(condition == null){
            condition = BookLoanStatus.RETURNED;
        }
        bookLoan.setStatus(condition);

        // Fine
        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);
        bookLoan.setNotes("Book returned by User");

        // Update book availability
        if (condition != BookLoanStatus.LOST){
            BookEntity book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            bookRepository.save(book);
        }
        BookLoanEntity savedBookLoan = bookLoanRepository.save(bookLoan);
        return  bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO renewCheckOut(RenewalRequest request) throws Exception {

        // Validate Book loan
        BookLoanEntity bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(
                ()-> new Exception("Book Loan not found")
        );

        // Check if we can renew
        if (!bookLoan.canRenew()){
            throw new Exception("Book Loan is not renewable.");
        }
        // Update the due date
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(request.getExtensionDays()));
        bookLoan.setRenewalCount(bookLoan.getRenewalCount()+1);
        bookLoan.setNotes("Book renewed by User");
        BookLoanEntity savedBookLoan = bookLoanRepository.save(bookLoan);

        return  bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) {

        UserEntity user = userService.getCurrentUser();
        Page<BookLoanEntity> bookLoanPage;

        if (status != null){
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUser(status, user, pageable);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUserId(user.getId(), pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoanEntity> bookLoanPage) {
        List<BookLoanDTO> bookLoanDTOS = bookLoanPage.getContent()
                .stream().map(bookLoanMapper::toDTO)
                .toList();

        return new PageResponse<>(
                bookLoanDTOS,
                bookLoanPage.getNumber(),
                bookLoanPage.getSize(),
                bookLoanPage.getTotalElements(),
                bookLoanPage.getTotalPages(),
                bookLoanPage.isFirst(),
                bookLoanPage.isLast(),
                bookLoanPage.isEmpty()
        );
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {

        size = Math.min(size, 100);
        size = Math.max(size, 0);

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request) {

        Pageable pageable = createPageable(request.getPage(), request.getSize(), request.getSortBy(), request.getSortDirection());
        Page<BookLoanEntity> bookLoanPage;

        if (Boolean.TRUE.equals(request.getOverdueOnly())){
            bookLoanPage = bookLoanRepository.findOverDueBookLoans(LocalDate.now(), pageable);

        } else if(request.getBookId() != null){
            bookLoanPage = bookLoanRepository.findByBookId(request.getBookId(), pageable);

        } else if(request.getStatus() != null){
            bookLoanPage = bookLoanRepository.findByStatus(request.getStatus(), pageable);

        } else if(request.getStartDate() != null && request.getEndDate() != null){
            bookLoanPage = bookLoanRepository.findBookLoanByDateRange(request.getStartDate(), request.getEndDate(), pageable);
        } else {
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public Integer updateOverdueBookLoans() {

        Pageable pageable = PageRequest.of(0, 1000);
        Page<BookLoanEntity> overDuePage = bookLoanRepository.findOverDueBookLoans(LocalDate.now(), pageable);

        int updatedCount = 0;
        for (BookLoanEntity bookLoan : overDuePage.getContent()) {

            if(bookLoan.getStatus() == BookLoanStatus.CHECKED_OUT){
                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverdue(true);

                // Calculate overDue days
//                int overDueDays = fineCalculationService.calculateOverDueDays(bookLoan.getDueDate(), LocalDate.now());
                int overDueDays = calculateOverDueDays(bookLoan.getDueDate(), LocalDate.now());
                bookLoan.setOverdueDays(overDueDays);

                //Calculate fine
                //BigDecimal fine = fineCalculaionService.calculateOverdueFine(bookLoan);

                bookLoanRepository.save(bookLoan);
                updatedCount++;

            }
        }
        return  updatedCount;
    }

    public int calculateOverDueDays(LocalDate dueDate, LocalDate today) {
        if (today.isBefore(dueDate) || today.isEqual(dueDate)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate, today);
    }
}
